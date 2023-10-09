package com.asd.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.asd.Service.TransactionService;
import jakarta.inject.Inject;
import jakarta.transaction.InvalidTransactionException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.Map;

@Path("jdbc/transaction")
public class TransactionController {

@Inject
TransactionService transactionService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @POST
    @Path("/TOKEN_START")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response StartTransaction(Map<String, Object> requestData) {
        System.out.println("Start Transaction ");
        try {
         return Response.ok(transactionService.startTransaction((String) requestData.get("token"))).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        catch (Exception e) {
            e.printStackTrace();

            System.out.println(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("InternalServerError").build();

        }

    }
    @POST
    @Path("/COMMIT_AUTO")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response MakeSetCommitAutoAsTrue(Map<String, Object> requestData) {
        System.out.println("Make set Commit auto as true from controller " + requestData);
        String token = (String) requestData.get("token");

        try {
            transactionService.SetCommitAutoAsTrue(token);
            return Response.ok("Commit as true ended successfully").build();
        } catch (InvalidTransactionException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        catch (Exception e) {
            e.printStackTrace();

            System.out.println(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("InternalServerError").build();

        }
    }

    @POST
    @Path("/CLOSE_CONNECTION")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response closeConnection(Map<String, Object> requestData) {
        System.out.println("Close connection from controller " + requestData);
        String token = (String) requestData.get("token");

        try {
            transactionService.CloseConnection(token);
            return Response.ok("Coneection closed successfully").build();
        } catch (InvalidTransactionException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        catch (Exception e) {
            e.printStackTrace();

            System.out.println(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("InternalServerError").build();

        }
    }


    @POST
    @Path("/COMMIT")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)

    public Response commit(Map<String, Object> requestData) {

        System.out.println("Start Commit ");
        String token = (String) requestData.get("token");

        try {
            transactionService.commit( token);
            return  Response.ok("true").build();
        } catch (InvalidTransactionException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        catch (Exception e) {
            e.printStackTrace();

            System.out.println(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("InternalServerError").build();

        }
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    @Path("/ROLLBACK")
    public Response rollback(Map<String, Object> requestData) {
        String token = (String) requestData.get("token");

        try {
            transactionService.rollback(token);
            return  Response.ok("true").build();
        } catch (InvalidTransactionException e) {
            e.printStackTrace();

            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("InternalServerError").build();

        }
    }
}

