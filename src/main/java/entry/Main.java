
package entry ;

/**
 *
 * @author ryahiaoui
 */

import org.wildfly.swarm.Swarm ;
import org.jboss.shrinkwrap.api.ShrinkWrap ;
import org.wildfly.swarm.jaxrs.JAXRSArchive ;
import org.wildfly.swarm.config.undertow.Server ;
import org.wildfly.swarm.undertow.UndertowFraction ;
import org.wildfly.swarm.config.undertow.BufferCache ;
import org.wildfly.swarm.config.undertow.server.Host ;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset ;
import org.wildfly.swarm.management.ManagementFraction ;
import org.wildfly.swarm.config.management.SecurityRealm ;
import org.wildfly.swarm.datasources.DatasourcesFraction ;
import org.wildfly.swarm.config.undertow.ServletContainer ;
import org.wildfly.swarm.config.undertow.server.HttpsListener ;
import org.wildfly.swarm.config.undertow.HandlerConfiguration ;
import org.wildfly.swarm.config.undertow.servlet_container.JSPSetting ;
import org.wildfly.swarm.management.console.ManagementConsoleFraction ;
import org.wildfly.swarm.config.management.security_realm.SslServerIdentity ;
import org.wildfly.swarm.config.undertow.servlet_container.WebsocketsSetting ;

public class Main {
   
    public static void main(String[] args) throws Exception {
             
        String driverClassName  = "org.postgresql.Driver"            ;
        String connectionUrl    = "jdbc:postgresql://127.0.0.1/aero" ;
        String userName         = "db_user"                          ;
        String password         = "password_user"                    ;
           
        String tmpDirProperty = "java.io.tmpdir"                     ;
 
        String tmpDir         = System.getProperty( tmpDirProperty ) ;
 
        System.out.println( " ** VFS = " + tmpDir )                  ;

        System.setProperty("java.net.preferIPv4Stack" , "true")      ;
        
        Swarm  swarm          = new Swarm() ;
        
        DatasourcesFraction dataSource = new DatasourcesFraction().jdbcDriver("org.postgresql" ,
                                            (d) -> {
                                                    d.driverClassName( driverClassName)                     ;
                                                    d.xaDatasourceClass("org.postgresql.xa.PGXADataSource") ;
                                                    d.driverModuleName("org.postgresql")                    ; 
                                             }).dataSource("MyPU", (ds) -> {
                                                    ds.driverName(driverClassName.replace(".Driver", ""))   ;
                                                    ds.connectionUrl(connectionUrl)                         ;
                                                    ds.userName(userName)                                   ;
                                                    ds.password(password)                                   ;
                                                    ds.jndiName("java:jboss/datasources/Scheduler")         ;
                                                })                                                          ;
        swarm.fraction(dataSource) ;

        ManagementFraction securityRealm = ManagementFraction.createDefaultFraction()
                                                             .httpInterfaceManagementInterface((iface) -> {
                                               iface.allowedOrigin("http://localhost:8080") ;
                                               iface.securityRealm("ManagementRealm")       ;
                                           }).securityRealm("ManagementRealm" , (realm) ->  {
                                                    realm.inMemoryAuthentication (
                                                        (authn) -> {
                                                           authn.add("rya", "rac021", true) ;
                                                    }) ;
                                                    realm.inMemoryAuthorization(
                                                        (authz) -> {
                                                            authz.add("rya", "admin") ;
                                                    }) ;
                                              }) ;
        /*
          Enable HTTPS 
          Ex of Generating a Certificat using JDK : 
          -genkey -v -keystore my-release-key.keystore -alias alias_name -keyalg RSA -keysize 2048 -validity 10000 
        */
        
        if( System.getProperty("transport") != null && 
            System.getProperty("transport").equalsIgnoreCase("HTTPS")) {
             
            System.out.println(" **** Enable HTTP **** " ) ;
             
            securityRealm.securityRealm(new SecurityRealm("SSLRealm")
                         .sslServerIdentity( new SslServerIdentity<>()
                            .keystorePath("/opt/jdk/jdk1.8.0_111/jre/bin/my-release-key.keystore")
                            .keystorePassword("yahiaoui021")
                            .alias("alias_name")
                            .keyPassword("yahiaoui021")
                         )
            ) ;
           
            swarm.fraction(new UndertowFraction()
                 .server(new Server("default-server")
                 .httpsListener(new HttpsListener("default")
                 .securityRealm("SSLRealm")
                 .socketBinding("https"))
                 .host(new Host("default-host")))
                 .bufferCache(new BufferCache("default"))
                 .servletContainer(new ServletContainer("default")
                 .websocketsSetting(new WebsocketsSetting())
                 .jspSetting(new JSPSetting()))
                 .handlerConfiguration(new HandlerConfiguration())) ; 

               /*
               swarm.fraction(UndertowFraction.createDefaultFraction()
                    .server("ssl-server", server -> server.httpsListener( new HttpsListener("https")
                      .securityRealm("SSLRealm")
                      .socketBinding("https")
                    ))
               ) ;
               */
        }

        swarm.fraction( securityRealm ) ;
        swarm.fraction(new ManagementConsoleFraction().contextRoot("/console")) ;
        
        swarm.start() ;

        JAXRSArchive deployment = ShrinkWrap.create(JAXRSArchive.class, "jax-d.war") ;

       /* Swagger Fraction -- Not implemented yet 
       
       deployment.as(SwaggerArchive.class)
                 .setResourcePackages("org.inra.swarm").setTitle("My Application API")
                 .setLicenseUrl("http://myapplication.com/license.txt")
                 .setLicense("Use at will").setContextRoot("/tacos")
                 .setDescription("This is a description of my API")
                 .setHost("api.myapplication.com").setContact("help@myapplication.com")
                 .setPrettyPrint(true).setSchemes( "http", "https")
                 .setTermsOfServiceUrl("http://myapplication.com/tos.txt")
                 .setVersion("1.0") ;
       */
       
       /** Persistence xml **/
       deployment.addAsWebInfResource(new ClassLoaderAsset("META-INF/persistence.xml" ,
       Main.class.getClassLoader()) , "classes/META-INF/persistence.xml") ;
       
       /** beans xml **/
       deployment.addAsWebInfResource(new ClassLoaderAsset("WEB-INF/beans.xml" ,
       Main.class.getClassLoader()) , "beans.xml") ;
        
       /** moxy property **/
       deployment.addAsWebInfResource(new ClassLoaderAsset("com/rac021/jax/api/streamers/jaxb.properties" , 
                                        Main.class.getClassLoader()) ,
                                       "classes/com/rac021/jax/api/streamers/jaxb.properties") ;
       
       /** Packages **/
       deployment.addPackage("com.rac021.jaxd.cors")           ;
       deployment.addPackage("com.rac021.jaxd.service.planes") ;
       deployment.addPackage("com.rac021.jaxd.security.provider.override") ;
       
       /** Deploy **/
       deployment.addAllDependencies(true) ;
       swarm.deploy( deployment)           ;

    }
    
}

