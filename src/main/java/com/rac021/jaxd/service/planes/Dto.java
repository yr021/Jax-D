
package com.rac021.jaxd.service.planes ;

import java.io.Serializable ;
import com.rac021.jax.api.manager.IDto ;
import javax.xml.bind.annotation.XmlType ;
import com.rac021.jax.api.qualifiers.RootDto ;
import javax.xml.bind.annotation.XmlAccessType ;
import javax.xml.bind.annotation.XmlAccessorType ;
import com.rac021.jax.api.qualifiers.ResultColumn ;

/**
 *
 * @author R.Yahiaoui
 */

@RootDto
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "model", "total_passengers", "distance_km" } )

public class Dto implements IDto, Serializable                      {

    @ResultColumn( index = 0 )  private String  model               ;

    @ResultColumn( index = 1 )  private Integer total_passengers    ;

    @ResultColumn( index = 2 )  private Integer distance_km         ;

}
