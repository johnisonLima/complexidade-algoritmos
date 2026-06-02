package br.edu.ifba.nobreaks.clientes;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifba.nobreaks.clientes.impl.ClienteImpl;
import br.edu.ifba.nobreaks.clientes.impl.NoBreak;
import br.edu.ifba.nobreaks.clientes.impl.SensoriamentoImpl;

public class App {

    private static final int TOTAL_NOBREAKS = 1;

    public static void main(String[] args) throws Exception {
        List<Thread> processos = new ArrayList<>();

        for (int i = 0; i < TOTAL_NOBREAKS; i++) {
            String id = "NBLP-" + (i + 1);

            ClienteImpl cliente = new ClienteImpl();
            cliente.configurar(new NoBreak(id, "único"), new SensoriamentoImpl());

            Thread processo = new Thread(cliente);
            processos.add(processo);
            processo.start();
        }
    
        for (Thread processo: processos) {
            processo.join();
        }

        System.out.println("leituras enviadas");
    }
}
