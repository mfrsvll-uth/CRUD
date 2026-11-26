package com.mycompany.proyectojavadb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ProyectoJavaDB {

static Scanner entrada = new Scanner(System.in, "UTF-8");

    public static void main(String[] args) {

        int opcion;
         System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));
System.setIn(new java.io.BufferedInputStream(System.in));

        try {
Connection conexion = ConexionDB.conectar();
if (conexion == null) {
                System.out.println("No se pudo establecer la conexión.");
                return;
            }

            System.out.println("==================================");
            System.out.println("   SISTEMA DE REGISTRO DE ALUMNOS");
            System.out.println("==================================");

            do {
                System.out.println("\n========== MENU PRINCIPAL ==========");
                System.out.println("1. Registrar alumno");
                System.out.println("2. Consultar alumnos");
                System.out.println("3. Actualizar alumno");
                System.out.println("4. Eliminar alumno");
                System.out.println("5. Salir");
                System.out.print("Seleccione una opción: ");

                opcion = entrada.nextInt();
                entrada.nextLine();

                switch (opcion) {

                    case 1:
                        registrarAlumno(conexion);
                        break;

                    case 2:
                        consultarAlumnos(conexion);
                        break;

                    case 3:
                        actualizarAlumno(conexion);
                        break;

                    case 4:
                        eliminarAlumno(conexion);
                        break;

                    case 5:
                        System.out.println("Saliendo del sistema...");
                        break;

                    default:
                        System.out.println("Opción no válida.");
                }

            } while (opcion != 5);

          conexion.close();
       } catch (Exception e) {
    System.out.println("Error al ejecutar el sistema: " + e.getMessage());
}
    }

    // CREATE: Registrar alumno
    public static void registrarAlumno(Connection conexion) {

        String nombre;
        String correo;

        System.out.println("\n========== REGISTRAR ALUMNO ==========");

        System.out.print("Ingrese el nombre: ");
        nombre = entrada.nextLine();

        System.out.print("Ingrese el correo: ");
        correo = entrada.nextLine();

        String sql = "INSERT INTO Alumnos (nombre, correo) VALUES (?, ?)";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, correo);

            int resultado = ps.executeUpdate();

            if (resultado > 0) {
                System.out.println("\nAlumno registrado correctamente.");
                System.out.println("\nLISTADO ACTUALIZADO:");
                consultarAlumnos(conexion);
            }

        } catch (SQLException e) {
            System.out.println("Error al registrar: " + e.getMessage());
        }
    }

    // READ: Consultar alumnos
    public static void consultarAlumnos(Connection conexion) {

        String sql = "SELECT id, nombre, correo FROM Alumnos ORDER BY id ASC";

        System.out.println("\n========== LISTADO DE ALUMNOS ==========");

        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.printf("%-10s %-30s %-35s%n",
                    "ID", "NOMBRE", "CORREO");

            System.out.println("--------------------------------------------------------------");

            boolean hayRegistros = false;

            while (rs.next()) {

                hayRegistros = true;

                System.out.printf("%-10d %-30s %-35s%n",
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"));
            }

            if (!hayRegistros) {
                System.out.println("No hay alumnos registrados.");
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar: " + e.getMessage());
        }
    }

    // UPDATE: Actualizar alumno
    public static void actualizarAlumno(Connection conexion) {

        System.out.println("\n========== ACTUALIZAR ALUMNO ==========");

        System.out.print("Ingrese el ID del alumno: ");
        int id = entrada.nextInt();
        entrada.nextLine();

        System.out.print("Ingrese el nuevo nombre: ");
        String nombre = entrada.nextLine();

        System.out.print("Ingrese el nuevo correo: ");
        String correo = entrada.nextLine();

        String sql = "UPDATE Alumnos SET nombre = ?, correo = ? WHERE id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, correo);
            ps.setInt(3, id);

            int resultado = ps.executeUpdate();

            if (resultado > 0) {
                System.out.println("Alumno actualizado correctamente.");
                consultarAlumnos(conexion);
            } else {
                System.out.println("No existe un alumno con ese ID.");
            }

        } catch (SQLException e) {
            System.out.println("Error al actualizar: " + e.getMessage());
        }
    }

    // DELETE: Eliminar alumno
    public static void eliminarAlumno(Connection conexion) {

        System.out.println("\n========== ELIMINAR ALUMNO ==========");

        System.out.print("Ingrese el ID del alumno que desea eliminar: ");
        int id = entrada.nextInt();
        entrada.nextLine();

        System.out.print("¿Está seguro de eliminar este alumno? (S/N): ");
        String confirmacion = entrada.nextLine();

        if (confirmacion.equalsIgnoreCase("S")) {

            String sql = "DELETE FROM Alumnos WHERE id = ?";

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {

                ps.setInt(1, id);

                int resultado = ps.executeUpdate();

                if (resultado > 0) {
                    System.out.println("Alumno eliminado correctamente.");
                    consultarAlumnos(conexion);
                } else {
                    System.out.println("No existe un alumno con ese ID.");
                }

            } catch (SQLException e) {
                System.out.println("Error al eliminar: " + e.getMessage());
            }

        } else {
            System.out.println("Eliminación cancelada.");
        }
    }
}
