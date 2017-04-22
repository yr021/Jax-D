
package com.rac021.jaxd.service.planes ;

import com.rac021.jax.api.manager.IResource ;
import com.rac021.jax.api.qualifiers.SqlQuery ;
import com.rac021.jax.api.qualifiers.ResourceRegistry ;

/**
 *
 * @author R.Yahiaoui
 */

@ResourceRegistry("resourcePlane")

public class Resource implements IResource {

    @SqlQuery("filtered_planes")
    private final  String PLANES = " SELECT                                 " +
                                   " model , total_passengers , distance_km " +
                                   " FROM aircraft "                          ;
}
