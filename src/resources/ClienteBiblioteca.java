package resources;

import java.io.*;
import java.net.*;
import java.util.*;

// Cliente que interactúa con el servidor para hacer consultas sobre los libros.

public class ClienteBiblioteca {

    private static final String HOST = "localhost"; // Dirección del servidor
    private static final int PORT = 11593; // Puerto del servidor

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            try (Socket socket = new Socket(HOST, PORT)) {
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                System.out.println("Conectado al servidor");

                while (true) {
                    System.out.println("Seleccione una opción:");
                    System.out.println("1. Consultar libro por ISBN");
                    System.out.println("2. Consultar libro por título");
                    System.out.println("3. Consultar libros por autor");
                    System.out.println("4. Añadir un libro");
                    System.out.println("0. Salir");

                    int opcion = scanner.nextInt();
                    scanner.nextLine();

                    switch (opcion) {
                        case 1: // Consultar por ISBN
                            System.out.println("Ingrese el ISBN:");
                            String isbn = scanner.nextLine();
                            out.writeObject("CONSULTAR_ISBN");
                            out.writeObject(isbn);
                            out.flush(); // Asegurar que se envíen los datos
                            Object respuesta = in.readObject();
                            System.out.println("Respuesta del servidor: " + respuesta);
                            break;

                        case 2: // Consultar por título
                            System.out.println("Ingrese el título:");
                            String titulo = scanner.nextLine();
                            out.writeObject("CONSULTAR_TITULO");
                            out.writeObject(titulo);
                            out.flush();
                            respuesta = in.readObject();
                            System.out.println("Respuesta del servidor: " + respuesta);
                            break;

                        case 3: // Consultar por autor
                            System.out.println("Ingrese el autor:");
                            String autor = scanner.nextLine();
                            out.writeObject("CONSULTAR_AUTOR");
                            out.writeObject(autor);
                            out.flush();
                            respuesta = in.readObject();
                            System.out.println("Respuesta del servidor: " + respuesta);
                            break;

                        case 4: // Añadir un libro
                            System.out.println("Ingrese el ISBN del libro:");
                            String nuevoIsbn = scanner.nextLine();
                            System.out.println("Ingrese el título del libro:");
                            String nuevoTitulo = scanner.nextLine();
                            System.out.println("Ingrese el autor del libro:");
                            String nuevoAutor = scanner.nextLine();
                            System.out.println("Ingrese el precio del libro:");
                            double precio = scanner.nextDouble();
                            scanner.nextLine();

                            Libro nuevoLibro = new Libro(nuevoIsbn, nuevoTitulo, nuevoAutor, precio);
                            out.writeObject("AÑADIR_LIBRO");
                            out.writeObject(nuevoLibro);
                            out.flush();
                            respuesta = in.readObject();
                            System.out.println("Respuesta del servidor: " + respuesta);
                            break;

                        case 5: // Salir
                            System.out.println("Saliendo...");
                            return;

                        default:
                            System.out.println("Opción no válida.");
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error durante la comunicación con el servidor: " + e.getMessage());
            }
        }
    }
}