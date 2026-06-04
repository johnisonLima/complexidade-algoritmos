package br.edu.ifba.minasaquaticas;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifba.minasaquaticas.impl.ClienteImpl;
import br.edu.ifba.minasaquaticas.impl.Mina;
import br.edu.ifba.minasaquaticas.impl.SensoriamentoImpl;

public class App {
    // Quantidade de minas simuladas
    private static final int TOTAL_MINAS = 10;

    public static void main(String[] args) throws Exception {

        List<Thread> processos = new ArrayList<>();

        for (int i = 0; i < TOTAL_MINAS; i++) {

            ClienteImpl cliente = new ClienteImpl(); 

            cliente.configurar(
                new Mina(i + 1, (i + 1) * 2),
                new SensoriamentoImpl()
            );

            Thread processo = new Thread(cliente);

            processos.add(processo);

            processo.start();
        }

        for (Thread processo : processos) {
            processo.join();
        }

        System.out.println("Todas as leituras foram enviadas.");
    }
}