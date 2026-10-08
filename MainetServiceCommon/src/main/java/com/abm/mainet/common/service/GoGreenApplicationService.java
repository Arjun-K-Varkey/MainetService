package com.abm.mainet.common.service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.abm.mainet.common.integration.dto.ApplyForGoGreenDTO;

import io.swagger.annotations.*;

@Path("/goGreenService")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api("/goGreenService")
public interface GoGreenApplicationService {

    @POST
    @Path("/getGoGreenData")
    @ApiOperation(value = "Return Go Green data",
            notes = "Return Go Green data",
            response = ApplyForGoGreenDTO.class)
    ApplyForGoGreenDTO getGoGreenApplicationDetails(
            @ApiParam(value = "Go Green dashboard request", required = true)
            ApplyForGoGreenDTO dto);

    @POST
    @Path("/updateGoGreenData")
    @ApiOperation(value = "Update Go Green data",
            notes = "Update Go Green data",
            response = ApplyForGoGreenDTO.class)
    ApplyForGoGreenDTO updateGoGreenApplicationDetails(
            @ApiParam(value = "Go Green dashboard request", required = true)
            ApplyForGoGreenDTO dto);
    
    @POST
    @Path("/unsubscribePropGoGreenData")
    @ApiOperation(value = "Unsubscribe Property Go Green data",
            notes = "Unsubscribe Property Go Green data",
            response = ApplyForGoGreenDTO.class)
    boolean unsubscribePropGoGreenData(
            @ApiParam(value = "Go Green dashboard request", required = true)
            ApplyForGoGreenDTO dto);
}
