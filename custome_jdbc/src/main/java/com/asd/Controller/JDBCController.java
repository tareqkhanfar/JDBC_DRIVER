package com.asd.Controller;

import com.asd.Entity.Request;
import com.asd.Entity.RequestSelect;
import com.asd.Exception.NoMorePagesToFetchException;
import com.asd.MetaData.MetaData;
import com.asd.Service.JdbcService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;

@Path("/jdbc")
public class JDBCController {



@Inject
JdbcService jdbcService  ;


    @GET
    @Path("/metrics")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getMetrics() {
        return Response.ok(jdbcService.getMetrics()).build();
    }



    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/AUTHENTICATE")
    public Response Authenticate (Request request) {
        try {
            MetaData metaData = jdbcService.Authunticate(request);

            return   Response.ok(metaData).build();
        } catch (SQLException e) {
            e.printStackTrace();

            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();

        } catch (Exception e) {
            e.printStackTrace();
            return   Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();

        }
    }


    @Path("SELECT/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response fetch (RequestSelect requestSelect) throws SQLException {
        String decodedQueryParameterValue = URLDecoder.decode(requestSelect.getQuery(), StandardCharsets.UTF_8);

        try {
           List<LinkedHashMap<String, Object>> list;
            list = jdbcService.fetchByQuery(requestSelect.getToken() ,decodedQueryParameterValue);
            return Response.ok(list).build();

        }
        catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }

        catch (Exception e ) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }

    }
    @Path("SELECT_PAGING/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response fetchWithPaging (RequestSelect requestSelect) throws SQLException {
        String decodedQueryParameterValue = URLDecoder.decode(requestSelect.getQuery(), StandardCharsets.UTF_8);
        requestSelect.setQuery(decodedQueryParameterValue);


        try {
            List<LinkedHashMap<String, Object>> list;
            list = jdbcService.fetchByQueryWithPaging(requestSelect.getToken() ,requestSelect);
            return Response.ok(list).build();
        }
        catch (NoMorePagesToFetchException e ) {
            return Response.status(408).entity(e.getMessage()).build();
        }
        catch (Exception e ) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }

    }
    @Path("INSERT/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response Insert(Map<String, Object> requestData) {

        String encodedQuery = (String) requestData.get("query");
        String token = (String) requestData.get("token");

        // URL decode the encoded query
        String decodedQuery = URLDecoder.decode(encodedQuery, StandardCharsets.UTF_8);
        System.out.println("Query : " + decodedQuery);
        System.out.println("token : " + token);


        try {
            Integer numberOfRowsAffected = jdbcService.insert(token, decodedQuery);
            System.out.println("insert done ");
            return Response.ok(numberOfRowsAffected).build();
        } catch (Exception e) {
            // Handle exceptions appropriately
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }



    @Path("UPDATE/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response Update (Map<String, Object> requestData) throws SQLException {


        String encodedQuery = (String) requestData.get("query");
        String token = (String) requestData.get("token");

        // URL decode the encoded query
        String decodedQuery = URLDecoder.decode(encodedQuery, StandardCharsets.UTF_8);

        try {
            Integer numberOfRowsAffected  = jdbcService.Update(token ,decodedQuery);
            return Response.ok(numberOfRowsAffected).build();
        }

catch (SQLException e ) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
catch (Exception e ) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }

    }

    @Path("DELETE/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response Delete ( Map<String, Object> requestData) throws SQLException {
        String encodedQuery = (String) requestData.get("query");
        String token = (String) requestData.get("token");

        // URL decode the encoded query
        String decodedQuery = URLDecoder.decode(encodedQuery, StandardCharsets.UTF_8);

        try {
    Integer numberOfRowsAffected = jdbcService.Delete(token ,decodedQuery);


    return Response.ok(numberOfRowsAffected).build();
}
catch (SQLException e ) {
    return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
}
catch (Exception e ) {
    return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
}

    }

    @Path("CREATE_TABLE/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response createTable (Map<String, Object> requestData) throws SQLException {
        String encodedQuery = (String) requestData.get("query");
        String token = (String) requestData.get("token");

        // URL decode the encoded query
        String decodedQuery = URLDecoder.decode(encodedQuery, StandardCharsets.UTF_8);


        try {


    Integer numberOfRowsAffected = jdbcService.createTable(token , decodedQuery);

    return Response.ok(numberOfRowsAffected).build();
}
catch (SQLException e ) {
    return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
}
catch (Exception e ) {
    return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
}

    }

    @POST
    @Path("test/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response testCheck (String testname) {
        System.out.println(testname + "is ok ");
        return Response.ok(testname +"is ok ").build();
    }




}
