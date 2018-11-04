package controle.soap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Banco {
	Connection con;
	PreparedStatement ps;
	ResultSet rs;

	public void conectar() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/compra", "root", "");
	}

	public Integer efetuar(String operacao, Integer valor, Integer idContaLocal, Integer saldoLocal, Integer idContaDestino, Integer saldoDestino) throws Exception {
		if (operacao.equals("deposito")) {
			saldoLocal += valor;
		} else if (operacao.equals("saque")) {
			saldoLocal -= valor;
		} else if (operacao.equals("transferencia")) {
			saldoLocal -= valor;
			saldoDestino += valor;
			atualizarSaldo(saldoDestino, idContaDestino);
		}
		atualizarSaldo(saldoLocal, idContaLocal);
		inserir(operacao, valor, idContaLocal, idContaDestino);
		return saldoLocal;
	}

	public void atualizarSaldo(Integer saldo, Integer idConta) throws Exception {
		conectar();
		ps = con.prepareStatement("update Conta set saldo=? where idConta=?");
		ps.setDouble(1, saldo);
		ps.setInt(2, idConta);
		ps.execute();
		con.close();
	}

	public void inserir(String operacao, Integer valor, Integer idContaLocal, Integer idContaDestino) throws Exception {
		conectar();
		ps = con.prepareStatement("insert into Transacao values (null,?,?,now(),?,?)");
		ps.setString(1, operacao);
		ps.setDouble(2, valor);
		ps.setInt(3, idContaLocal);
		ps.setInt(4, idContaDestino);
		ps.execute();
		con.close();
	}

}
