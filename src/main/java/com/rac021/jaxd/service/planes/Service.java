
package com.rac021.jaxd.service.planes ;

import javax.ws.rs.GET ;
import javax.inject.Inject ;
import javax.ws.rs.Produces ;
import javax.ws.rs.QueryParam ;
import javax.ws.rs.HeaderParam ;
import javax.ws.rs.core.UriInfo ;
import javax.ws.rs.core.Context ;
import javax.ws.rs.core.Response ;
import javax.annotation.PostConstruct ;
import com.rac021.jax.api.qualifiers.security.Public ;
import com.rac021.jax.api.qualifiers.ServiceRegistry ;
import com.rac021.jax.api.streamers.StreamerOutputXml ;
import com.rac021.jax.api.qualifiers.ResourceRegistry ;
import com.rac021.jax.api.streamers.StreamerOutputJson ;
import com.rac021.jax.api.streamers.StreamerOutputXmlEncrypted ;
import com.rac021.jax.api.streamers.StreamerOutputJsonEncrypted ;

/**
 *
 * @author R.Yahioaui
 */

@ServiceRegistry("planes")
@Public
/* @Secured(policy = Policy.CustomSignOn) */

public class Service  {

    @Inject
    @ResourceRegistry("resourcePlane")
    Resource Resource ;

    @Context /* Optionnal */
    UriInfo uriInfo   ;
    
    @Inject 
    StreamerOutputXml streamerOutputXml   ;
    
    @Inject
    StreamerOutputJson streamerOutputJson ;
    
    @Inject 
    StreamerOutputXmlEncrypted streamerOutputXmlEncrypted ;
    
    @Inject 
    StreamerOutputJsonEncrypted streamerOutputJsonEncrypted ;

    
    @PostConstruct
    public void init() {
    }

    public Service() {
    }
   
    @GET
    @Produces("xml/plain")
    public Response getResourceAsXml( @HeaderParam("sort")            String filterdParam     , 
                                      @QueryParam("model")            String model            , 
                                      @QueryParam("total_passengers") String total_passengers ,
                                      @QueryParam("distance_km")      String distance_km   )  {    

        /**
        * DefaultStreamerConfigurator defaultStreamerConfigurator = new
        * DefaultStreamerConfigurator() ;
        * defaultStreamerConfigurator.setNbrCores(2) ;
        * defaultStreamerConfigurator.setRatio(4) ;
        * defaultStreamerConfigurator.setRecorderLenght(16000) ;
        * streamerOutputXml.setStreamerConfigurator(defaultStreamerConfigurator) ;
        */
            
        return Response.status( Response.Status.OK )
                       .entity( streamerOutputXml.wrapResource( Resource     , 
                                                                Dto.class    , 
                                                                filterdParam , 
                                                                uriInfo.getQueryParameters() ) )
                       .build() ;
    }

    @GET
    @Produces("xml/encrypted")
    public Response getResourceAsXmlEncrypted ( @HeaderParam("sort")            String filterdParam     ,                                 
                                                @QueryParam("model")            String model            , 
                                                @QueryParam("total_passengers") String total_passengers ,
                                                @QueryParam("distance_km")      String distance_km   )  {    
        /** 
        * DefaultStreamerConfigurator defaultStreamerConfigurator = new
        * DefaultStreamerConfigurator() ;
        * defaultStreamerConfigurator.setNbrCores(2) ;
        * defaultStreamerConfigurator.setRatio(4) ;
        * defaultStreamerConfigurator.setRecorderLenght(16000) ;
        * streamerOutputXmlEncrypted.setStreamerConfigurator(defaultStreamerConfigurator) ;
        */
            
         return Response.status( Response.Status.OK )
                        .entity(streamerOutputXmlEncrypted.wrapResource( Resource     , 
                                                                         Dto.class    , 
                                                                         filterdParam , 
                                                                         uriInfo.getQueryParameters() ) )
                        .build() ;
    }
     
    @GET
    @Produces("json/plain")
    public Response getResourceAsJson( @HeaderParam("sort")            String  filterdParam     , 
                                       @QueryParam("model")            String  model            , 
                                       @QueryParam("total_passengers") String  total_passengers ,
                                       @QueryParam("distance_km")      String  distance_km   )  {    
        /**
        * DefaultStreamerConfigurator defaultStreamerConfigurator = new
        * DefaultStreamerConfigurator() ;
        * defaultStreamerConfigurator.setNbrCores(2) ;
        * defaultStreamerConfigurator.setRatio(4) ;
        * defaultStreamerConfigurator.setRecorderLenght(16000) ;
        * streamerOutputJson.setStreamerConfigurator(defaultStreamerConfigurator) ;
        */
  
        return Response.status( Response.Status.OK )
                       .entity(streamerOutputJson.wrapResource( Resource     , 
                                                                Dto.class    , 
                                                                filterdParam , 
                                                                uriInfo.getQueryParameters() ) )
                       .build() ;
    }
    
    @GET
    @Produces("json/encrypted")
    public Response getResourceAsJsonEncrypted( @HeaderParam("sort")            String  filterdParam , 
                                                @QueryParam("model")            String  model        , 
                                                @QueryParam("total_passengets") String  total_pass   ,
                                                @QueryParam("distance_km")      String  distance_km  ) {    
     
        /**
        * DefaultStreamerConfigurator defaultStreamerConfigurator = new
        * DefaultStreamerConfigurator() ;
        * defaultStreamerConfigurator.setNbrCores(2) ;
        * defaultStreamerConfigurator.setRatio(4) ;
        * defaultStreamerConfigurator.setRecorderLenght(16000) ;
        * streamerOutputJsonEncrypted.setStreamerConfigurator(defaultStreamerConfigurator) ;
        */
  
        return Response.status( Response.Status.OK )
                       .entity( streamerOutputJsonEncrypted.wrapResource( Resource     , 
                                                                          Dto.class    , 
                                                                          filterdParam , 
                                                                          uriInfo.getQueryParameters() ) )
                       .build() ;
    }

}
