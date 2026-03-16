package br.com.ecomerce.ecomerce.model.enums;

public enum EstadoPagamento {

    PENDENTE(1, "Pendente"),
    QUITADO(2, "Quitado"),
    CANCELADO(3, "Cancelado");

    private int codigo;
    private String descricao;

    private EstadoPagamento(int codigo, String desricao) {
        this.codigo = codigo;
        this.descricao = desricao;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static EstadoPagamento toEnum(Integer codigo){
        if (codigo == null){
            return null;
        }
        for(EstadoPagamento x : EstadoPagamento.values()){
            if (codigo.equals(x.getCodigo())){
                return x;
            }
        }

        throw new IllegalArgumentException("Id inválido: " + codigo);
    }
}
