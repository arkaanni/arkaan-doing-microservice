package dev.arkaan.schoolapp.studentservice.resources;

import dev.arkaan.schoolapp.studentservice.api.Student;
import dev.arkaan.schoolapp.studentservice.db.StudentDao;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import java.util.List;

@Path("/students")
public class StudentResource {

    private final StudentDao studentDao;

    public StudentResource(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response getStudentById(@PathParam("id") String id) {
        return Response.ok(studentDao.getStudent(id)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addStudent(Student student) {
        try {
            studentDao.addOne(student);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllStudents() {
        try {
            List<Student> students = studentDao.getAll();
            return Response.ok(students)
                    .header(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
