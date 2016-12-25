package es.uclm.proyecto.modelo;

/**
 * Created by alber on 27/03/2016.
 */
public class Investigador {
    public int  _id;
    public String nombre;
    public String apellidos;
    public String email;

    public Investigador(int _id, String  nombre, String apellidos, String email) {
        this._id = _id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
    }
}
