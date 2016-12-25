package es.uclm.proyecto.modelo;

/**
 * Created by alber on 27/03/2016.
 */
public class Resultado {
    public int _id;
    public String am_l;     // Movimiento tobillo izquierdo
    public String am_r;     // Movimiento tobillo derecho

    public String pwo_l;    // Apoyo plantar sin soporte izquierdo
    public String pwo_r;    // Apoyo plantar sin soporte derecho
    public String pw_l;     // Apoyo plantar con soporte izquierdo
    public String pw_r;     // Apoyo plantar con soporte derecho

    public String sd_l;     // Stepping dorsal izquierdo
    public String sd_r;     // Stepping dorsal derecho
    public String sp_l;     // Stepping plantar izquierdo
    public String sp_r;     // Stepping plantar derecho

    public String coord;    // Coordinacion

    public String pp_ic_l;  //Posicion de la pata izquierda contacto inicial
    public String pp_ic_r;  //Posicion de la pata derecha contacto inicial
    public String pp_lo_l;  //Posicion de la pata izquierda final
    public String pp_lo_r;  //Posicion de la pata derecha final

    public String ti;       // Estabilidad del tronco
    public String tail;     // Posicion de la cola

    public Resultado(int _id,  String am_l, String am_r, String pwo_l, String pwo_r, String pw_l,
                             String pw_r, String sd_l, String sd_r, String sp_l, String sp_r,
                             String coord, String pp_ic_l, String pp_ic_r, String pp_lo_l,
                             String pp_lo_r, String ti, String tail) {
        this._id = _id;
        this.am_l = am_l;
        this.am_r = am_r;
        this.pwo_l = pwo_l;
        this.pwo_r = pwo_r;
        this.pw_l = pw_l;
        this.pw_r = pw_r;
        this.sd_l = sd_l;
        this.sd_r = sd_r;
        this.sp_l = sp_l;
        this.sp_r = sp_r;
        this.coord = coord;
        this.pp_ic_l = pp_ic_l;
        this.pp_ic_r = pp_ic_r;
        this.pp_lo_l = pp_lo_l;
        this.pp_lo_r = pp_lo_r;
        this.ti = ti;
        this.tail = tail;
    }

    public Resultado(String am_l, String am_r, String pwo_l, String pwo_r, String pw_l,
                     String pw_r, String sd_l, String sd_r, String sp_l, String sp_r,
                     String coord, String pp_ic_l, String pp_ic_r, String pp_lo_l,
                     String pp_lo_r, String ti, String tail) {
        this.am_l = am_l;
        this.am_r = am_r;
        this.pwo_l = pwo_l;
        this.pwo_r = pwo_r;
        this.pw_l = pw_l;
        this.pw_r = pw_r;
        this.sd_l = sd_l;
        this.sd_r = sd_r;
        this.sp_l = sp_l;
        this.sp_r = sp_r;
        this.coord = coord;
        this.pp_ic_l = pp_ic_l;
        this.pp_ic_r = pp_ic_r;
        this.pp_lo_l = pp_lo_l;
        this.pp_lo_r = pp_lo_r;
        this.ti = ti;
        this.tail = tail;
    }

    public Resultado (Resultado resultado){
        this._id = resultado._id;
        this.am_l = resultado.am_l;
        this.am_r = resultado.am_r;
        this.pwo_l = resultado.pwo_l;
        this.pwo_r = resultado.pwo_r;
        this.pw_l = resultado.pw_l;
        this.pw_r = resultado.pw_r;
        this.sd_l = resultado.sd_l;
        this.sd_r = resultado.sd_r;
        this.sp_l = resultado.sp_l;
        this.sp_r = resultado.sp_r;
        this.coord = resultado.coord;
        this.pp_ic_l = resultado.pp_ic_l;
        this.pp_ic_r = resultado.pp_ic_r;
        this.pp_lo_l = resultado.pp_lo_l;
        this.pp_lo_r = resultado.pp_lo_r;
        this.ti = resultado.ti;
        this.tail = resultado.tail;

    }

}

