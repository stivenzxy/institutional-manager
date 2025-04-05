package interfaces;

public interface Observable {
    public void notificar();
    public void adicionarObservador(Observador observador);
    public void removerObservador(Observador observador);
}
