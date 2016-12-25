package es.uclm.proyecto.modelo;

/**
 * Created by alber on 27/03/2016.
 */
public class Estudio {
    public int      _id;
    public int      idAnimal;
    public String   fecha;
    public String   hora;
    public int      tiempo;
    public int      idInvestigador1;
    public int      idInvestigador2;
    public int      idTipoTest;
    public int      resultado;
    public int      scorel;
    public int      scorer;
    public String   video;
    public String   comentarios;

    public Estudio(int idAnimal, String fecha, String hora, int tiempo,
                   int idInvestigador1, int idInvestigador2, int idTipoTest, int resultado,int scorel,int scorer,String video, String comentarios) {
        this.idAnimal = idAnimal;
        this.fecha = fecha;
        this.hora = hora;
        this.tiempo = tiempo;
        this.idInvestigador1 = idInvestigador1;
        this.idInvestigador2 = idInvestigador2;
        this.idTipoTest = idTipoTest;
        this.resultado = resultado;
        this.scorel =scorel;
        this.scorer =scorer;
        this.video = video;
        this.comentarios = comentarios;
    }

    public Estudio(int _id, int idAnimal, String fecha, String hora, int tiempo,
                   int idInvestigador1, int idInvestigador2, int idTipoTest, int resultado, int scorel, int scorer,String video, String comentarios) {
        this._id = _id;
        this.idAnimal = idAnimal;
        this.fecha = fecha;
        this.hora = hora;
        this.tiempo = tiempo;
        this.idInvestigador1 = idInvestigador1;
        this.idInvestigador2 = idInvestigador2;
        this.idTipoTest = idTipoTest;
        this.resultado = resultado;
        this.scorel =scorel;
        this.scorer =scorer;
        this.video = video;
        this.comentarios = comentarios;
    }

}
