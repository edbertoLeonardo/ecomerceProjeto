package br.com.ecomerce.ecomerce.model.enums;

public enum TipoCliente {

    PESSOAFISICA(1, "Pessoa física"),
    PESSOAJURIDICA(2, "Pessoa jurídica");

    private int codigo;
    private String descricao;

   private TipoCliente(int codigo, String desricao) {
       this.codigo = codigo;
       this.descricao = desricao;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static TipoCliente toEnum(Integer codigo){
        if (codigo == null){
            return null;
        }
        for(TipoCliente x : TipoCliente.values()){
            if (codigo.equals(x.getCodigo())){
                return x;
            }
        }

        throw new IllegalArgumentException("Id inválido: " + codigo);
    }
}
