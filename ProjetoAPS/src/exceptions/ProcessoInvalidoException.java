package exceptions;

public class ProcessoInvalidoException extends RuntimeException{
	public ProcessoInvalidoException(String msg) {
		super(msg);
	}
}
