package resources;

import java.io.Serializable;

/*
 * Clase que representa un libro en la biblioteca virtual.
 */

public class Libro implements Serializable {
    private static final long serialVersionUID = 1L; // Versión de serialización para compatibilidad.

    private String isbn;   // Identificador del libro.
    private String titulo; // Título del libro.
    private String autor;  // Autor del libro.
    private double precio; // Precio del libro.


    // Constructor completo


    public Libro(String isbn, String titulo, String autor, double precio) {
        if (isbn == null || isbn.isEmpty()) throw new IllegalArgumentException("ISBN incompleto");
        if (titulo == null || titulo.isEmpty()) throw new IllegalArgumentException("Título incompleto");
        if (autor == null || autor.isEmpty()) throw new IllegalArgumentException("Autor incompleto");
        if (precio <= 0) throw new IllegalArgumentException("El precio debe ser mayor que 0");

        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.precio = precio;
    }


    // Constructor por defecto


    public Libro() {}

    // Métodos getters y setters con validaciones para garantizar integridad de datos.
    // Se asegura que los datos sean válidos antes de ser asignados.

    public String getIsbn() { return isbn; }

    public void setIsbn(String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            throw new IllegalArgumentException("ISBN incompleto");
        }
        this.isbn = isbn;
    }

    public String getTitulo() { return titulo; }

    public void setTitulo(String titulo) {
        if (titulo == null || titulo.isEmpty()) {
            throw new IllegalArgumentException("Título incompleto");
        }
        this.titulo = titulo;
    }

    public String getAutor() { return autor; }

    public void setAutor(String autor) {
        if (autor == null || autor.isEmpty()) {
            throw new IllegalArgumentException("Autor incompleto");
        }
        this.autor = autor;
    }

    public double getPrecio() { return precio; }

    public void setPrecio(double precio) {
        if (precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que cero");
        }
        this.precio = precio;
    }


    //Método para mostrar los datos del libro


    @Override
    public String toString() {
        return "Libro{" +
                "ISBN='" + isbn + '\'' +
                ", Título='" + titulo + '\'' +
                ", Autor='" + autor + '\'' +
                ", Precio=" + precio +
                '}';
    }


    // Define cuándo dos libros se consideran iguales (por su ISBN).


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Libro libro = (Libro) obj;
        return isbn.equals(libro.isbn);
    }

    @Override
    public int hashCode() {
        return isbn.hashCode(); // Clave basada en el ISBN.
    }
}
