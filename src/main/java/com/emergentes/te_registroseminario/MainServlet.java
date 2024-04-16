package com.emergentes.te_registroseminario;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "MainServlet", urlPatterns = {"/MainServlet"})
public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String op = request.getParameter("op");
        Seminario objsem = new Seminario();

        int id, pos;
        HttpSession ses = request.getSession();
        ArrayList<Seminario> lista = (ArrayList<Seminario>) ses.getAttribute("listasem");

        switch (op) {
            case "nuevo":
                request.setAttribute("miobjcal", objsem);
                request.getRequestDispatcher("editar.jsp").forward(request, response);
                break;
            case "editar":
                id = Integer.parseInt(request.getParameter("id"));
                pos = buscarPorIndice(request, id);
                objsem = lista.get(pos);
                request.setAttribute("miobjcal", objsem);
                request.getRequestDispatcher("editar.jsp").forward(request, response);
                break;
            case "eliminar":
                id = Integer.parseInt(request.getParameter("id"));
                pos = buscarPorIndice(request, id);
                if (pos >= 0) {
                    lista.remove(pos);
                }
                request.setAttribute("listasem", lista);
                response.sendRedirect("index.jsp");
                break;
            default:
                //throw new AssertionError();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        HttpSession ses = request.getSession();
        ArrayList<Seminario> lista = (ArrayList<Seminario>) ses.getAttribute("listasem");
        Seminario objsem = new Seminario();
        objsem.setId(id);
        objsem.setFecha(request.getParameter("fecha"));
        objsem.setNombre(request.getParameter("nombre"));
        objsem.setApellidos(request.getParameter("apellidos"));
        objsem.setTurno(request.getParameter("opcion"));
        
         String[] seminariosSeleccionados = request.getParameterValues("opciones[]");
        String seminario = "";
        if (seminariosSeleccionados != null) {
            seminario = String.join("; ", seminariosSeleccionados);
        }
        objsem.setSeminario(seminario);
        
        if (id == 0) {
            int idnuevo = obtenerId(request);
            objsem.setId(idnuevo);
            lista.add(objsem);
        } else {
            int pos = buscarPorIndice(request, id);
            lista.set(pos, objsem);
        }
        request.setAttribute("listasem", lista);
        response.sendRedirect("index.jsp");
    }

    public int buscarPorIndice(HttpServletRequest request, int id) {
        HttpSession ses = request.getSession();
        ArrayList<Seminario> lista = (ArrayList<Seminario>) ses.getAttribute("listasem");

        int pos = -1;
        if (lista != null) {
            for (Seminario ele : lista) {
                ++pos;
                if (ele.getId() == id) {

                    break;
                }
            }
        }
        return pos;
    }

    public int obtenerId(HttpServletRequest request) {
        HttpSession ses = request.getSession();
        ArrayList<Seminario> lista = (ArrayList<Seminario>) ses.getAttribute("listasem");
        int idn = 0;
        for (Seminario ele : lista) {
            idn = ele.getId();
        }
        return idn + 1;
    }
}
