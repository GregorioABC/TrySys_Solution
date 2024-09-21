import java.util.Scanner;

public class SistemaRestaurante {
    private Mesa cabeca;

    class ItemPedido {
        String descricao;
        int quantidade;
        double total;

        ItemPedido(String descricao, int quantidade, double total) {
            this.descricao = descricao;
            this.quantidade = quantidade;
            this.total = total;
        }
    }

    class Pedido {
        ItemPedido item;
        Pedido proximo;

        Pedido(ItemPedido item) {
            this.item = item;
            this.proximo = null;
        }
    }

    class Mesa {
        int numero;
        String cliente;
        String status; // "livre" ou "ocupada"
        Pedido pedidos;
        Mesa proximo;

        Mesa(int numero, String cliente) {
            this.numero = numero;
            this.cliente = cliente;
            this.status = "ocupada"; // ao adicionar, a mesa é ocupada
            this.pedidos = null;
            this.proximo = null; // inicializa o próximo como nulo
        }
    }

    public void adicionarMesa(int numero, String cliente) {
        Mesa novaMesa = new Mesa(numero, cliente);
        if (cabeca == null) {
            // Primeira mesa, aponta para ela mesma
            cabeca = novaMesa;
            cabeca.proximo = cabeca;
        } else {
            // Insere nova mesa no final da lista circular
            Mesa atual = cabeca;
            while (atual.proximo != cabeca) {
                atual = atual.proximo;
            }
            atual.proximo = novaMesa;
            novaMesa.proximo = cabeca; // Mantém a circularidade
        }
        System.out.println("Mesa " + numero + " adicionada para o cliente " + cliente + ".");
    }

    public void adicionarPedido(int numeroMesa, String descricao, int quantidade, double total) {
        Mesa mesa = buscarMesa(numeroMesa);
        if (mesa != null && mesa.status.equals("ocupada")) {
            ItemPedido novoItem = new ItemPedido(descricao, quantidade, total);
            Pedido novoPedido = new Pedido(novoItem);
            novoPedido.proximo = mesa.pedidos;
            mesa.pedidos = novoPedido;
            System.out.println("Pedido adicionado na mesa " + numeroMesa + ".");
        } else {
            System.out.println("Mesa não encontrada ou está livre.");
        }
    }

    public void fecharConta(int numeroMesa) {
        Mesa mesa = buscarMesa(numeroMesa);
        if (mesa != null && mesa.status.equals("ocupada")) {
            double totalConta = 0;
            Pedido atual = mesa.pedidos;
            while (atual != null) {
                totalConta += atual.item.total;
                atual = atual.proximo;
                if (atual == mesa.pedidos) break; // Para evitar loop infinito
            }
            System.out.println("Total a pagar na mesa " + numeroMesa + ": R$ " + totalConta);
            mesa.status = "livre"; // A mesa é liberada
            mesa.pedidos = null; // Limpa os pedidos
        } else {
            System.out.println("Mesa não encontrada ou já está livre.");
        }
    }

    public Mesa buscarMesa(int numero) {
        if (cabeca == null) return null;
        Mesa atual = cabeca;
        do {
            if (atual.numero == numero) {
                return atual;
            }
            atual = atual.proximo;
        } while (atual != cabeca);
        return null; // Se a mesa não foi encontrada
    }

    public void imprimirMesas() {
        if (cabeca == null) {
            System.out.println("Nenhuma mesa cadastrada.");
            return;
        }
        Mesa atual = cabeca;
        do {
            System.out.println("Mesa: " + atual.numero + ", Cliente: " + atual.cliente + ", Status: " + atual.status);
            if (atual.pedidos != null) {
                System.out.println("Pedidos:");
                Pedido pedidoAtual = atual.pedidos;
                do {
                    System.out.println(" - " + pedidoAtual.item.descricao + ": " + pedidoAtual.item.quantidade + " (R$ " + pedidoAtual.item.total + ")");
                    pedidoAtual = pedidoAtual.proximo;
                } while (pedidoAtual != null && pedidoAtual != atual.pedidos); // Para evitar loop infinito
            }
            atual = atual.proximo;
        } while (atual != cabeca);
    }

    public  void executar(Scanner scanner) {
        String opcao;

        do {
            System.out.println("Sistema de Restaurante:");
            System.out.println("1. Adicionar Mesa");
            System.out.println("2. Adicionar Pedido");
            System.out.println("3. Fechar Conta");
            System.out.println("4. Imprimir Mesas");
            System.out.println("0. Sair");
            System.out.print("Opção: ");
            opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    System.out.print("Número da mesa: ");
                    int numeroMesa = scanner.nextInt();
                    scanner.nextLine(); // Limpar o buffer do teclado
                    System.out.print("Nome do cliente: ");
                    String cliente = scanner.nextLine();
                    adicionarMesa(numeroMesa, cliente);
                    break;
                case "2":
                    System.out.print("Número da mesa: ");
                    numeroMesa = scanner.nextInt();
                    scanner.nextLine(); // Limpar o buffer do teclado
                    System.out.print("Descrição do pedido: ");
                    String descricao = scanner.nextLine();
                    System.out.print("Quantidade: ");
                    int quantidade = scanner.nextInt();
                    System.out.print("Total a pagar: ");
                    double total = scanner.nextDouble();
                    scanner.nextLine(); // Limpar o buffer do teclado
                    adicionarPedido(numeroMesa, descricao, quantidade, total);
                    break;
                case "3":
                    System.out.print("Número da mesa: ");
                    numeroMesa = scanner.nextInt();
                    scanner.nextLine(); // Limpar o buffer do teclado
                    fecharConta(numeroMesa);
                    break;
                case "4":
                    imprimirMesas();
                    break;
                case "0":
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (!opcao.equals("0"));
    }

    public static void main(String[] args) {
        SistemaRestaurante sistema = new SistemaRestaurante();
        Scanner scanner = new Scanner(System.in);
        sistema.executar(scanner);
        scanner.close();
    }
}
