package es.uclm.proyecto.modelo;

/**
 * Created by alber on 27/03/2016.
 */
public class Animal {

    public int _id;
    public String codigo;
    public int idEspecie;
    public String cepa;
    public String transgenico;
    public String sexo;
    public String fechaNam;
    public String fechaCir;
    public String tratamiento;
    public String modeloLesion;
    public String origen;
    public String experimento;

    public Animal(String codigo, int  idEspecie, String cepa, String transgenico, String sexo,
                  String fechaNam, String fechaCir, String tratamiento, String modeloLesion,
                  String origen, String experimento) {
        this.codigo = codigo;
        this.idEspecie = idEspecie;
        this.cepa = cepa;
        this.transgenico = transgenico;
        this.sexo = sexo;
        this.fechaNam = fechaNam;
        this.fechaCir = fechaCir;
        this.tratamiento = tratamiento;
        this.modeloLesion = modeloLesion;
        this.origen = origen;
        this.experimento = experimento;
    }

    public Animal(int _id, String codigo, int  idEspecie, String cepa, String transgenico, String sexo,
                  String fechaNam, String fechaCir, String tratamiento, String modeloLesion,
                  String origen, String experimento) {
        this._id = _id;
        this.codigo = codigo;
        this.idEspecie = idEspecie;
        this.cepa = cepa;
        this.transgenico = transgenico;
        this.sexo = sexo;
        this.fechaNam = fechaNam;
        this.fechaCir = fechaCir;
        this.tratamiento = tratamiento;
        this.modeloLesion = modeloLesion;
        this.origen = origen;
        this.experimento = experimento;

    }
}
