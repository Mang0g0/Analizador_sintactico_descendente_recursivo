/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.principal;

import java.util.List;


public class ASDR implements Parser{


    private int i = 0;
    private boolean hayErrores = false;
    private Token preanalisis;
    private final List<Token> tokens;

    //TABLA
    //string terminales[]={"select", };

    public ASDR(List<Token> tokens){
        this.tokens = tokens;
        preanalisis = this.tokens.get(i);
    }

    @Override
    public boolean parse() {
        Q();

        if(preanalisis.tipo == TipoToken.EOF && !hayErrores){
            System.out.println("Consulta correcta");
            return  true;
        }else {
            System.out.println("Se encontraron errores");
        }
        return false;
    }

    // Q -> select D from T
    private void Q(){
        match(TipoToken.SELECT);
        D();
        match(TipoToken.FROM);
        T();
    }
    // T -> T2t1
    private void T(){
        if(hayErrores)
            return;
        T2();
        T1();
    }

    // T2 -> id T3
    private void T2(){
        if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
            match(TipoToken.IDENTIFICADOR);
            T3();
        }
        else{
            hayErrores = true;
            Principal.error(i, "'" + TipoToken.IDENTIFICADOR + "' esperado.");
            //System.out.println("T2 Se esperaba un 'identificador'");
        }
    }
    // T1 -> ,T | Ɛ
    private void T1(){
        if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.COMA){
            match(TipoToken.COMA);
            T();
        }/*
        else{
            hayErrores = true;
            System.out.println("T1 Se esperaba una 'coma'");
        }*/
    }
    // T3 -> id | Ɛ
    private void T3(){
        if(hayErrores)
            return;
        if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
            match(TipoToken.IDENTIFICADOR);
        }
        /*
        else{
            hayErrores = true;
            System.out.println("T3 Se esperaba un 'identificador'");
        }*/
    }
    // D -> distinct P | P
    private void D(){
        if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.DISTINCT){
            match(TipoToken.DISTINCT);
            P();
        }
        else if (preanalisis.tipo == TipoToken.ASTERISCO
                || preanalisis.tipo == TipoToken.IDENTIFICADOR) {
            P();
        }
        else{
            hayErrores = true;
            Principal.error(i, "'" + TipoToken.IDENTIFICADOR + "' o '" + TipoToken.ASTERISCO + "' o '" + TipoToken.IDENTIFICADOR + "' esperado.");
            //System.out.println("Se esperaba 'distinct' or '*' or 'identificador'");
        }
    }

    // P -> * | A
    private void P(){
        if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.ASTERISCO){
            match(TipoToken.ASTERISCO);
        }
        else if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
            A();
        }
        else{
            hayErrores = true;
            Principal.error(i, "'" + TipoToken.ASTERISCO + "' o '" + TipoToken.IDENTIFICADOR + "' esperado.");
            //System.out.println("Se esperaba '*' or 'identificador'");
        }
    }

    // A -> A2 A1
    private void A(){
        if(hayErrores)
            return;

        A2();
        A1();
    }

    // A2 -> id A3
    private void A2(){
        if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
            match(TipoToken.IDENTIFICADOR);
            A3();
        }
        else{
            hayErrores = true;
            Principal.error(i, "'" + TipoToken.ASTERISCO + "' esperado.");
            //System.out.println("A2 Se esperaba un 'identificador'");
        }
    }

    // A1 -> ,A | Ɛ
    private void A1(){
        if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.COMA){
            match(TipoToken.COMA);
            A();
        }
    }

    // A3 -> . id | Ɛ
    private void A3(){
        if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.PUNTO){
            match(TipoToken.PUNTO);
            match(TipoToken.IDENTIFICADOR);
        }
    }


    private void match(TipoToken tt){
        if(preanalisis.tipo == tt){
            /*---------------------------------------------
            Como el preanalisis y el token coincidieron, 
            avanza el preanalisis al siguiente token:1
            ---------------------------------------------*/
            i++;
            preanalisis = tokens.get(i);
            //---------------------------------------------
        }
        else{
            /*---------------------------------------------
            Si hay un error termina el programa:
            ---------------------------------------------*/
            hayErrores = true;
            Principal.error(i, "'" + tt + "' esperado.");
            //System.out.println("Error encontrado: '"+tt+"' esperado.");
        }

    }

}