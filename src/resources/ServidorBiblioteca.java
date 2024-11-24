package resources;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.locks.*;

/*
 * Servidor que gestiona los libros de la biblioteca virtual.
 * Permite la interacción con múltiples clientes mediante hilos.
 */

public class ServidorBiblioteca {

    private static final int PORT = 11593; // Puerto del servidor
    private List<Libro> libros = new ArrayList<>(); // Lista para almacenar libro
    private final ReentrantLock lock = new ReentrantLock(); // Lock para sincronizar el acceso a la lista de libros

    public ServidorBiblioteca() {
        libros.add(new Libro("1105", "Mi querida Lucía", "La Vecina Rubia", 20.85));
        libros.add(new Libro("2111", "Un corazón por navidad", "Sophie Jomain", 16.95));
        libros.add(new Libro("2705", "Todas esas cosas que te diré mañana", "Elísabet Benavent", 25.99));
        libros.add(new Libro("2208", "La asistenta", "Freida McFadden", 18.90));
        libros.add(new Libro("2104", "El niño que perdió la guerra", "Julia Navarro", 23.65));
        libros.add(new Libro("2414", "La isla de la mujer dormida", "Arturo Pérez-Reverte", 21.75));
        libros.add(new Libro("6666", "El monje que vendió su ferrari", "Robin Sharma", 9.95));
        libros.add(new Libro("0101", "Un cuento perfecto", "Elísabet Benavent", 16.99));
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) { // Crea un ServerSocket que escucha en el puerto 12345
            System.out.println("Servidor iniciado en el puerto " + PORT);

            while (true) {
                // Acepta la conexión de un cliente
                Socket clienteSocket = serverSocket.accept();
                System.out.println("Cliente conectado");

                // Crea un nuevo hilo para manejar la comunicación con el cliente
                new Thread(new ClienteHandler(clienteSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
        }
    }

    private class ClienteHandler implements Runnable {
        private Socket socket;

        public ClienteHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                 ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

                String operacion = (String) in.readObject();  // Lee la operación solicitada por el cliente
                Object respuesta = null;

                // Dependiendo de la operación, se realiza la consulta correspondiente

                switch (operacion) {
                    case "CONSULTAR_ISBN":
                        String isbnConsulta = (String) in.readObject();
                        respuesta = consultarPorIsbn(isbnConsulta);
                        break;
                    case "CONSULTAR_TITULO":
                        String tituloConsulta = (String) in.readObject();
                        respuesta = consultarPorTitulo(tituloConsulta);
                        break;
                    case "CONSULTAR_AUTOR":
                        String autorConsulta = (String) in.readObject();
                        respuesta = consultarPorAutor(autorConsulta);
                        break;
                    case "AÑADIR_LIBRO":
                        Libro nuevoLibro = (Libro) in.readObject();
                        respuesta = añadirLibro(nuevoLibro);
                        break;
                    default:
                        respuesta = "Operación no válida";
                }

                out.writeObject(respuesta); // Envia la respuesta al cliente
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error en la comunicación con el cliente: " + e.getMessage());
            } finally {
                try {
                    if (socket != null && !socket.isClosed()) {
                        socket.close(); // Cierra la conexión del cliente
                    }
                } catch (IOException e) {
                    System.err.println("Error al cerrar la conexión del cliente: " + e.getMessage());
                }
            }
        }
    }

    // Métodos para consultar los libros

    private Libro consultarPorIsbn(String isbn) {
        return libros.stream()
                .filter(libro -> libro.getIsbn().equals(isbn))
                .findFirst()
                .orElse(null);
    }

    private Libro consultarPorTitulo(String titulo) {
        return libros.stream()
                .filter(libro -> libro.getTitulo().equalsIgnoreCase(titulo))
                .findFirst()
                .orElse(null);
    }

    private List<Libro> consultarPorAutor(String autor) {
        return libros.stream()
                .filter(libro -> libro.getAutor().equalsIgnoreCase(autor))
                .toList();
    }

    private String añadirLibro(Libro libro) {
        lock.lock();
        try {
            if (libros.contains(libro)) {
                return "El libro ya está registrado.";
            }
            libros.add(libro);
            return "Libro añadido exitosamente.";
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        new ServidorBiblioteca().start(); // Inicia el servidor
    }
}

