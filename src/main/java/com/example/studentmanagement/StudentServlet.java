package com.example.studentmanagement;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "StudentServlet", value = "/student")
public class StudentServlet extends HttpServlet {
    private static List<Student> studentList;

    static  {
        studentList = new ArrayList<>();

        studentList.add(new Student(1, "Nguyễn Văn A", 4.9));
        studentList.add(new Student(2, "Nguyễn Văn B", 5.6));
        studentList.add(new Student(3, "Nguyễn Văn C", 9.8));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        if(action == null) {
            action = "";
        }

        switch (action) {
            case "create": // Khi nhấn vào nút create => create.jsp
                request.getRequestDispatcher("student/create.jsp").forward(request, response);
                break;
            case "edit":
                int id  = Integer.parseInt(request.getParameter("id"));
                Student student = findById(id);

                request.setAttribute("student", student);
                request.getRequestDispatcher("student/edit.jsp").forward(request, response);
                break;
            default:
                request.setAttribute("studentList", studentList);
                request.getRequestDispatcher("student/list.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        if(action == null) {
            action = "";
        }

        switch (action) {
            case "create":
                createStudent(request, response);
//                request.setAttribute("studentList", studentList);
//                request.getRequestDispatcher("student/list.jsp").forward(request, response);
                response.sendRedirect("student?message=" + URLEncoder.encode("Thêm mới thành công", "UTF-8")); // GET
                break;
            case "edit":
                editStudent(request, response);
//                request.setAttribute("studentList", studentList);
//                request.getRequestDispatcher("student/list.jsp").forward(request, response);
                response.sendRedirect("student?message=" + URLEncoder.encode("Chỉnh sửa thành công", "UTF-8")); // GET
                break;
        }
    }

    private void createStudent(HttpServletRequest request, HttpServletResponse response) {
        Student student = new Student();
        student.setId((int) (Math.random() * 10000));
        student.setName(request.getParameter("name"));
        student.setScore(Double.parseDouble(request.getParameter("score")));

        studentList.add(student);
    }

    private void editStudent(HttpServletRequest request, HttpServletResponse response) {
        int id = Integer.parseInt(request.getParameter("id"));
        Student student = findById(id);
        student.setName(request.getParameter("name"));
        student.setScore(Double.parseDouble(request.getParameter("score")));
    }

    private Student findById(int id) {
        for(Student student : studentList) {
            if(student.getId() == id) {
                return student;
            }
        }

        return null;
    }
}
