package org.example.lidyvariedadesdesktop;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import org.example.lidyvariedadesdesktop.service.ClienteService;
import org.example.lidyvariedadesdesktop.service.ProdutoService;
import org.example.lidyvariedadesdesktop.service.VendaService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class HelloController {

    @FXML private TableView<Produto> tabelaProdutos;
    @FXML private TableColumn<Produto, Long> colId;
    @FXML private TableColumn<Produto, String> colCodigo;
    @FXML private TableColumn<Produto, String> colNome;
    @FXML private TableColumn<Produto, String> colCategoria;
    @FXML private TableColumn<Produto, String> colMarca;
    @FXML private TableColumn<Produto, BigDecimal> colPrecoCusto;
    @FXML private TableColumn<Produto, BigDecimal> colPrecoVenda;
    @FXML private TableColumn<Produto, Integer> colEstoque;
    @FXML private TableColumn<Produto, Integer> colEstoqueMinimo;
    

    @FXML private TextField txtPesquisa;
    @FXML private Label lblStatus;
    @FXML private Label lblConexao;

    // FXML Clientes
    @FXML private TableView<Cliente> tabelaClientes;
    @FXML private TableColumn<Cliente, Long> colClienteId;
    @FXML private TableColumn<Cliente, String> colClienteNome;
    @FXML private TableColumn<Cliente, String> colClienteTelefone;
    @FXML private TableColumn<Cliente, String> colClienteEndereco;
    @FXML private TextField txtPesquisaCliente;

    // FXML Vendas
    @FXML private ComboBox<Cliente> comboClienteVenda;
    @FXML private ComboBox<Produto> comboProdutoVenda;
    @FXML private Label lblEstoqueDisponivel;
    @FXML private TextField txtQtdItem;
    @FXML private TableView<ItemCarrinho> tabelaCarrinho;
    @FXML private TableColumn<ItemCarrinho, String> colCarrinhoProduto;
    @FXML private TableColumn<ItemCarrinho, Integer> colCarrinhoQtd;
    @FXML private TableColumn<ItemCarrinho, BigDecimal> colCarrinhoPreco;
    @FXML private TableColumn<ItemCarrinho, BigDecimal> colCarrinhoSubtotal;
    @FXML private ComboBox<String> comboFormaPagamento;
    @FXML private Label lblTotalVenda;

    // FXML Aba Vendas (Histórico / Relatório)
    @FXML private DatePicker dateInicio;
    @FXML private DatePicker dateFim;
    @FXML private TableView<Venda> tabelaVendas;
    @FXML private TableColumn<Venda, Long> colVendaId;
    @FXML private TableColumn<Venda, String> colVendaData;
    @FXML private TableColumn<Venda, String> colVendaCliente;
    @FXML private TableColumn<Venda, String> colVendaPagamento;
    @FXML private TableColumn<Venda, BigDecimal> colVendaTotal;
    @FXML private TableColumn<Venda, BigDecimal> colVendaLucro;
    @FXML private Label lblTotalVendas;
    @FXML private Label lblTotalLucro;

    private final ProdutoService produtoService = new ProdutoService();
    private final ObservableList<Produto> listaProdutos = FXCollections.observableArrayList();

    private final ClienteService clienteService = new ClienteService();
    private final VendaService vendaService = new VendaService();

    private final ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();
    private final ObservableList<ItemCarrinho> listaCarrinho = FXCollections.observableArrayList();
    private final ObservableList<Venda> listaVendas = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        // 1. Mapeia cada coluna para os atributos da classe Produto
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigoReferencia"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoriaNome"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marcaNome"));
        colPrecoCusto.setCellValueFactory(new PropertyValueFactory<>("precoCusto"));
        colPrecoVenda.setCellValueFactory(new PropertyValueFactory<>("precoVenda"));
        colEstoque.setCellValueFactory(new PropertyValueFactory<>("quantidadeEstoque"));
        colEstoqueMinimo.setCellValueFactory(new PropertyValueFactory<>("estoqueMinimo"));
        tabelaProdutos.setItems(listaProdutos);

        // 2. Mapeia colunas da tabela de clientes
        colClienteId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colClienteNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colClienteTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colClienteEndereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        tabelaClientes.setItems(listaClientes);

        // 3. Mapeia colunas da tabela de carrinho (venda)
        colCarrinhoProduto.setCellValueFactory(new PropertyValueFactory<>("produtoNome"));
        colCarrinhoQtd.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colCarrinhoPreco.setCellValueFactory(new PropertyValueFactory<>("precoUnitario"));
        colCarrinhoSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        tabelaCarrinho.setItems(listaCarrinho);

        // 4. Configura combos de venda
        comboClienteVenda.setItems(listaClientes);
        comboProdutoVenda.setItems(listaProdutos);
        comboFormaPagamento.getItems().addAll("Dinheiro", "PIX", "Cartão de Débito", "Cartão de Crédito");
        comboFormaPagamento.setValue("Dinheiro");

        // Atualiza label de estoque quando seleciona um produto no caixa
        comboProdutoVenda.setOnAction(e -> {
            Produto p = comboProdutoVenda.getValue();
            if (p != null) {
                lblEstoqueDisponivel.setText("Estoque: " + p.getQuantidadeEstoque());
            } else {
                lblEstoqueDisponivel.setText("Estoque: -");
            }
        });

        // 5. Mapeia colunas da tabela de histórico de vendas
        colVendaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colVendaData.setCellValueFactory(new PropertyValueFactory<>("dataFormatada"));
        colVendaCliente.setCellValueFactory(new PropertyValueFactory<>("clienteNome"));
        colVendaPagamento.setCellValueFactory(new PropertyValueFactory<>("formaPagamento"));
        colVendaTotal.setCellValueFactory(new PropertyValueFactory<>("totalVenda"));
        colVendaLucro.setCellValueFactory(new PropertyValueFactory<>("lucro"));
        tabelaVendas.setItems(listaVendas);

        // 6. Busca os dados ao abrir a tela
        onCarregarProdutos();
        onCarregarClientes();
        onRecarregarVendas();
    }

    // Carrega todos os produtos do backend
    @FXML
    public void onCarregarProdutos() {
        lblStatus.setText("Atualizando lista de produtos...");
        
        // Executa a chamada HTTP em uma thread separada para não travar a tela
        new Thread(() -> {
            try {
                List<Produto> produtos = produtoService.listarTodos();
                Platform.runLater(() -> {
                    listaProdutos.setAll(produtos);
                    lblStatus.setText("Total de produtos cadastrados: " + produtos.size());
                    lblConexao.setText("● Conectado à API");
                    lblConexao.setStyle("-fx-text-fill: #16a34a; -fx-font-weight: bold;");
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    lblStatus.setText("Erro ao conectar à API: " + e.getMessage());
                    lblConexao.setText("○ Desconectado");
                    lblConexao.setStyle("-fx-text-fill: #dc2626; -fx-font-weight: bold;");
                });
            }
        }).start();
    }

    // Pesquisa produto por nome
    @FXML
    public void onPesquisar() {
        String termo = txtPesquisa.getText();
        if (termo == null || termo.trim().isEmpty()) {
            onCarregarProdutos();
            return;
        }

        lblStatus.setText("Filtrando produtos por: '" + termo + "'...");
        new Thread(() -> {
            try {
                List<Produto> encontrados = produtoService.buscarPorNome(termo.trim());
                Platform.runLater(() -> {
                    listaProdutos.setAll(encontrados);
                    lblStatus.setText("Encontrados: " + encontrados.size() + " produto(s)");
                });
            } catch (Exception e) {
                Platform.runLater(() -> lblStatus.setText("Erro na busca: " + e.getMessage()));
            }
        }).start();
    }

    // Limpa a pesquisa e recarrega
    @FXML
    public void onLimparPesquisa() {
        txtPesquisa.clear();
        onCarregarProdutos();
    }

    // Abre modal para cadastrar novo produto
    @FXML
    public void onNovoProduto() {
        Dialog<Produto> dialog = new Dialog<>();
        dialog.setTitle("Cadastrar Novo Produto");
        dialog.setHeaderText("Preencha os dados do novo produto:");

        ButtonType salvarBtnType = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(salvarBtnType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField txtNome = new TextField();
        txtNome.setPromptText("Ex: Blusa Estampada");
        
        TextField txtCodigo = new TextField();
        txtCodigo.setPromptText("Ex: REF-001");

        // Caixa de seleção (ComboBox) para Marca
        ComboBox<Marca> comboMarca = new ComboBox<>();
        comboMarca.setPromptText("Selecione a Marca");
        comboMarca.setPrefWidth(220);
        List<Marca> marcas = produtoService.listarMarcas();
        comboMarca.getItems().addAll(marcas);
        if (!marcas.isEmpty()) {
            comboMarca.setValue(marcas.get(0));
        }

        // Caixa de seleção (ComboBox) para Categoria
        ComboBox<Categoria> comboCategoria = new ComboBox<>();
        comboCategoria.setPromptText("Selecione a Categoria");
        comboCategoria.setPrefWidth(220);
        List<Categoria> categorias = produtoService.listarCategorias();
        comboCategoria.getItems().addAll(categorias);
        if (!categorias.isEmpty()) {
            comboCategoria.setValue(categorias.get(0));
        }

        TextField txtPrecoCusto = new TextField("0.00");
        TextField txtPrecoVenda = new TextField("0.00");
        TextField txtEstoque = new TextField("0");
        TextField txtEstoqueMin = new TextField("0");

        grid.add(new Label("Nome:"), 0, 0);
        grid.add(txtNome, 1, 0);

        grid.add(new Label("Cód. Referência:"), 0, 1);
        grid.add(txtCodigo, 1, 1);

        grid.add(new Label("Marca:"), 0, 2);
        grid.add(comboMarca, 1, 2);

        grid.add(new Label("Categoria:"), 0, 3);
        grid.add(comboCategoria, 1, 3);

        grid.add(new Label("Preço de Custo:"), 0, 4);
        grid.add(txtPrecoCusto, 1, 4);

        grid.add(new Label("Preço de Venda:"), 0, 5);
        grid.add(txtPrecoVenda, 1, 5);

        grid.add(new Label("Estoque Atual:"), 0, 6);
        grid.add(txtEstoque, 1, 6);

        grid.add(new Label("Estoque Mínimo:"), 0, 7);
        grid.add(txtEstoqueMin, 1, 7);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == salvarBtnType) {
                Produto p = new Produto();
                p.setNome(txtNome.getText());
                p.setCodigoReferencia(txtCodigo.getText());
                p.setMarca(comboMarca.getValue());
                p.setCategoria(comboCategoria.getValue());

                try {
                    p.setPrecoCusto(new BigDecimal(txtPrecoCusto.getText().replace(",", ".")));
                    p.setPrecoVenda(new BigDecimal(txtPrecoVenda.getText().replace(",", ".")));
                    p.setQuantidadeEstoque(Integer.parseInt(txtEstoque.getText().trim()));
                    p.setEstoqueMinimo(Integer.parseInt(txtEstoqueMin.getText().trim()));
                } catch (Exception ex) {
                    // Mantém valores default se conversão falhar
                }
                return p;
            }
            return null;
        });

        Optional<Produto> result = dialog.showAndWait();
        result.ifPresent(produto -> {
            // Verifica se produto com mesmo nome ou referência já existe
            Optional<Produto> existente = listaProdutos.stream()
                .filter(p -> (p.getNome() != null && produto.getNome() != null 
                              && p.getNome().trim().equalsIgnoreCase(produto.getNome().trim()))
                          || (p.getCodigoReferencia() != null && !p.getCodigoReferencia().trim().isEmpty()
                              && produto.getCodigoReferencia() != null 
                              && p.getCodigoReferencia().trim().equalsIgnoreCase(produto.getCodigoReferencia().trim())))
                .findFirst();

            if (existente.isPresent()) {
                Produto prodExistente = existente.get();
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, 
                    "O produto '" + prodExistente.getNome() + "' já está cadastrado!\n" +
                    "Estoque atual: " + prodExistente.getQuantidadeEstoque() + " unidades.\n\n" +
                    "Deseja apenas ADICIONAR " + produto.getQuantidadeEstoque() + " unidades ao estoque existente?",
                    ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                confirm.setTitle("Produto Já Cadastrado");
                confirm.setHeaderText("Produto existente encontrado");
                Optional<ButtonType> resp = confirm.showAndWait();

                if (resp.isPresent() && resp.get() == ButtonType.YES) {
                    prodExistente.setQuantidadeEstoque(prodExistente.getQuantidadeEstoque() + produto.getQuantidadeEstoque());
                    if (produto.getPrecoVenda() != null && produto.getPrecoVenda().compareTo(BigDecimal.ZERO) > 0) {
                        prodExistente.setPrecoVenda(produto.getPrecoVenda());
                    }
                    if (produto.getPrecoCusto() != null && produto.getPrecoCusto().compareTo(BigDecimal.ZERO) > 0) {
                        prodExistente.setPrecoCusto(produto.getPrecoCusto());
                    }

                    new Thread(() -> {
                        try {
                            produtoService.atualizar(prodExistente.getId(), prodExistente);
                            Platform.runLater(this::onCarregarProdutos);
                        } catch (Exception e) {
                            Platform.runLater(() -> {
                                Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao atualizar estoque: " + e.getMessage());
                                alert.show();
                            });
                        }
                    }).start();
                    return;
                } else if (resp.isEmpty() || resp.get() == ButtonType.CANCEL) {
                    return;
                }
            }

            new Thread(() -> {
                try {
                    produtoService.cadastrar(produto);
                    Platform.runLater(this::onCarregarProdutos);
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao salvar: " + e.getMessage());
                        alert.show();
                    });
                }
            }).start();
        });
    }

    // Adiciona estoque rapidamente a um produto selecionado na tabela
    @FXML
    public void onAdicionarEstoque() {
        Produto selecionado = tabelaProdutos.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Por favor, selecione um produto na tabela para adicionar estoque.");
            alert.setTitle("Atenção");
            alert.setHeaderText("Nenhum produto selecionado");
            alert.show();
            return;
        }

        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Repor Estoque");
        dialog.setHeaderText("Produto: " + selecionado.getNome() + "\nEstoque atual: " + selecionado.getQuantidadeEstoque() + " unidades");
        dialog.setContentText("Quantidade a ADICIONAR:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(qtdStr -> {
            try {
                int qtdAdicionar = Integer.parseInt(qtdStr.trim());
                if (qtdAdicionar <= 0) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "A quantidade deve ser maior que zero.");
                    alert.show();
                    return;
                }

                selecionado.setQuantidadeEstoque(selecionado.getQuantidadeEstoque() + qtdAdicionar);

                new Thread(() -> {
                    try {
                        produtoService.atualizar(selecionado.getId(), selecionado);
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Estoque atualizado com sucesso!\nNovo total: " + selecionado.getQuantidadeEstoque() + " unidades.");
                            alert.setTitle("Estoque Atualizado");
                            alert.setHeaderText("Sucesso");
                            alert.show();
                            onCarregarProdutos();
                        });
                    } catch (Exception e) {
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao atualizar estoque: " + e.getMessage());
                            alert.show();
                        });
                    }
                }).start();

            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Por favor, digite um número inteiro válido.");
                alert.show();
            }
        });
    }

    // Exclui o produto selecionado na tabela
    @FXML
    public void onExcluirProduto() {
        Produto selecionado = tabelaProdutos.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Por favor, selecione um produto na tabela.");
            alert.show();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Deseja realmente excluir '" + selecionado.getNome() + "'?");
        confirm.setTitle("Confirmar Exclusão");
        Optional<ButtonType> resposta = confirm.showAndWait();

        if (resposta.isPresent() && resposta.get() == ButtonType.OK) {
            new Thread(() -> {
                try {
                    produtoService.excluir(selecionado.getId());
                    Platform.runLater(this::onCarregarProdutos);
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao excluir: " + e.getMessage());
                        alert.show();
                    });
                }
            }).start();
        }
    }

    // ==========================================
    //           AÇÕES DE CLIENTES
    // ==========================================

    @FXML
    public void onCarregarClientes() {
        lblStatus.setText("Atualizando lista de clientes...");
        new Thread(() -> {
            try {
                List<Cliente> clientes = clienteService.listarTodos();
                Platform.runLater(() -> {
                    listaClientes.setAll(clientes);
                    lblStatus.setText("Total de clientes: " + clientes.size());
                });
            } catch (Exception e) {
                Platform.runLater(() -> lblStatus.setText("Erro ao carregar clientes: " + e.getMessage()));
            }
        }).start();
    }

    @FXML
    public void onPesquisarCliente() {
        String termo = txtPesquisaCliente.getText();
        if (termo == null || termo.trim().isEmpty()) {
            onCarregarClientes();
            return;
        }

        lblStatus.setText("Buscando clientes por: '" + termo + "'...");
        new Thread(() -> {
            try {
                List<Cliente> encontrados = clienteService.buscar(termo.trim());
                Platform.runLater(() -> {
                    listaClientes.setAll(encontrados);
                    lblStatus.setText("Encontrados: " + encontrados.size() + " cliente(s)");
                });
            } catch (Exception e) {
                Platform.runLater(() -> lblStatus.setText("Erro na busca de clientes: " + e.getMessage()));
            }
        }).start();
    }

    @FXML
    public void onLimparPesquisaCliente() {
        txtPesquisaCliente.clear();
        onCarregarClientes();
    }

    @FXML
    public void onNovoCliente() {
        Dialog<Cliente> dialog = new Dialog<>();
        dialog.setTitle("Cadastrar Novo Cliente");
        dialog.setHeaderText("Preencha os dados do cliente:");

        ButtonType salvarBtnType = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(salvarBtnType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField txtNome = new TextField();
        txtNome.setPromptText("Ex: Maria da Silva");

        TextField txtTelefone = new TextField();
        txtTelefone.setPromptText("Ex: (84) 99999-9999");

        TextField txtEndereco = new TextField();
        txtEndereco.setPromptText("Ex: Rua das Flores, 123");

        grid.add(new Label("Nome:"), 0, 0);
        grid.add(txtNome, 1, 0);
        grid.add(new Label("Telefone:"), 0, 1);
        grid.add(txtTelefone, 1, 1);
        grid.add(new Label("Endereço:"), 0, 2);
        grid.add(txtEndereco, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == salvarBtnType) {
                if (txtNome.getText() == null || txtNome.getText().trim().isEmpty()) {
                    return null;
                }
                Cliente c = new Cliente();
                c.setNome(txtNome.getText().trim());
                c.setTelefone(txtTelefone.getText().trim());
                c.setEndereco(txtEndereco.getText().trim());
                return c;
            }
            return null;
        });

        Optional<Cliente> result = dialog.showAndWait();
        result.ifPresent(cliente -> {
            new Thread(() -> {
                try {
                    clienteService.cadastrar(cliente);
                    Platform.runLater(this::onCarregarClientes);
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao salvar cliente: " + e.getMessage());
                        alert.show();
                    });
                }
            }).start();
        });
    }

    @FXML
    public void onExcluirCliente() {
        Cliente selecionado = tabelaClientes.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Por favor, selecione um cliente na tabela.");
            alert.show();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Deseja realmente excluir o cliente '" + selecionado.getNome() + "'?");
        confirm.setTitle("Confirmar Exclusão");
        Optional<ButtonType> resposta = confirm.showAndWait();

        if (resposta.isPresent() && resposta.get() == ButtonType.OK) {
            new Thread(() -> {
                try {
                    clienteService.excluir(selecionado.getId());
                    Platform.runLater(this::onCarregarClientes);
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao excluir cliente: " + e.getMessage());
                        alert.show();
                    });
                }
            }).start();
        }
    }

    // ==========================================
    //           AÇÕES DE VENDA (PDV)
    // ==========================================

    @FXML
    public void onAdicionarItemCarrinho() {
        Produto produto = comboProdutoVenda.getValue();
        if (produto == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Por favor, selecione um produto para adicionar.");
            alert.show();
            return;
        }

        int qtd;
        try {
            qtd = Integer.parseInt(txtQtdItem.getText().trim());
            if (qtd <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "A quantidade deve ser um número inteiro positivo.");
            alert.show();
            return;
        }

        if (produto.getQuantidadeEstoque() < qtd) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Estoque insuficiente! Disponível: " + produto.getQuantidadeEstoque());
            alert.show();
            return;
        }

        // Se o produto já estiver no carrinho, apenas soma a quantidade
        boolean jaExiste = false;
        for (ItemCarrinho item : listaCarrinho) {
            if (item.getProduto() != null && item.getProduto().getId().equals(produto.getId())) {
                int novaQtd = item.getQuantidade() + qtd;
                if (produto.getQuantidadeEstoque() < novaQtd) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Estoque insuficiente para a soma! Disponível: " + produto.getQuantidadeEstoque());
                    alert.show();
                    return;
                }
                item.setQuantidade(novaQtd);
                item.setSubtotal(item.getPrecoUnitario().multiply(BigDecimal.valueOf(novaQtd)));
                jaExiste = true;
                break;
            }
        }

        if (!jaExiste) {
            listaCarrinho.add(new ItemCarrinho(produto, qtd));
        }

        tabelaCarrinho.refresh();
        atualizarTotalVenda();
        txtQtdItem.setText("1");
    }

    @FXML
    public void onRemoverItemCarrinho() {
        ItemCarrinho selecionado = tabelaCarrinho.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Por favor, selecione um item no carrinho para remover.");
            alert.show();
            return;
        }
        listaCarrinho.remove(selecionado);
        atualizarTotalVenda();
    }

    @FXML
    public void onLimparCarrinho() {
        listaCarrinho.clear();
        atualizarTotalVenda();
    }

    private void atualizarTotalVenda() {
        BigDecimal total = BigDecimal.ZERO;
        for (ItemCarrinho item : listaCarrinho) {
            if (item.getSubtotal() != null) {
                total = total.add(item.getSubtotal());
            }
        }
        lblTotalVenda.setText(String.format("TOTAL: R$ %.2f", total));
    }

    @FXML
    public void onFinalizarVenda() {
        if (listaCarrinho.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "O carrinho está vazio! Adicione ao menos um produto.");
            alert.show();
            return;
        }

        String formaPagamento = comboFormaPagamento.getValue();
        if (formaPagamento == null || formaPagamento.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Selecione a forma de pagamento.");
            alert.show();
            return;
        }

        VendaRequest request = new VendaRequest();
        Cliente cliente = comboClienteVenda.getValue();
        if (cliente != null) {
            request.setClienteId(cliente.getId());
        }
        request.setFormaPagamento(formaPagamento);

        List<ItemVendaRequest> itensRequest = new java.util.ArrayList<>();
        for (ItemCarrinho item : listaCarrinho) {
            ItemVendaRequest itemReq = new ItemVendaRequest();
            itemReq.setProdutoId(item.getProduto().getId());
            itemReq.setQuantidade(item.getQuantidade());
            itensRequest.add(itemReq);
        }
        request.setItens(itensRequest);

        lblStatus.setText("Finalizando venda no servidor...");
        new Thread(() -> {
            try {
                vendaService.finalizarVenda(request);
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Venda realizada com sucesso!");
                    alert.setTitle("Sucesso");
                    alert.setHeaderText("Venda Finalizada");
                    alert.show();

                    onLimparCarrinho();
                    comboClienteVenda.setValue(null);
                    comboProdutoVenda.setValue(null);
                    lblEstoqueDisponivel.setText("Estoque: -");

                    // Atualiza estoque na aba de produtos e o histórico de vendas
                    onCarregarProdutos();
                    onRecarregarVendas();
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao finalizar venda: " + e.getMessage());
                    alert.show();
                    lblStatus.setText("Erro na venda: " + e.getMessage());
                });
            }
        }).start();
    }

    // ==========================================
    //       AÇÕES DE HISTÓRICO DE VENDAS
    // ==========================================

    @FXML
    public void onRecarregarVendas() {
        lblStatus.setText("Atualizando histórico de vendas...");
        new Thread(() -> {
            try {
                List<Venda> vendas = vendaService.listarTodas();
                Platform.runLater(() -> {
                    listaVendas.setAll(vendas);
                    atualizarResumoVendas(vendas);
                    lblStatus.setText("Total de vendas registradas: " + vendas.size());
                });
            } catch (Exception e) {
                Platform.runLater(() -> lblStatus.setText("Erro ao carregar vendas: " + e.getMessage()));
            }
        }).start();
    }

    @FXML
    public void onFiltrarVendas() {
        LocalDate inicio = dateInicio != null ? dateInicio.getValue() : null;
        LocalDate fim = dateFim != null ? dateFim.getValue() : null;

        lblStatus.setText("Filtrando vendas...");
        new Thread(() -> {
            try {
                List<Venda> todas = vendaService.listarTodas();
                List<Venda> filtradas = new java.util.ArrayList<>();

                for (Venda v : todas) {
                    if (v.getDataVenda() == null) continue;
                    LocalDate dataVenda = v.getDataVenda().toLocalDate();

                    boolean aposInicio = (inicio == null) || !dataVenda.isBefore(inicio);
                    boolean antesFim = (fim == null) || !dataVenda.isAfter(fim);

                    if (aposInicio && antesFim) {
                        filtradas.add(v);
                    }
                }

                Platform.runLater(() -> {
                    listaVendas.setAll(filtradas);
                    atualizarResumoVendas(filtradas);
                    lblStatus.setText("Vendas no período: " + filtradas.size());
                });
            } catch (Exception e) {
                Platform.runLater(() -> lblStatus.setText("Erro ao filtrar vendas: " + e.getMessage()));
            }
        }).start();
    }

    private void atualizarResumoVendas(List<Venda> vendas) {
        BigDecimal totalVendido = BigDecimal.ZERO;
        BigDecimal totalLucro = BigDecimal.ZERO;

        for (Venda v : vendas) {
            if (v.getTotalVenda() != null) {
                totalVendido = totalVendido.add(v.getTotalVenda());
            }
            if (v.getLucro() != null) {
                totalLucro = totalLucro.add(v.getLucro());
            }
        }

        if (lblTotalVendas != null) {
            lblTotalVendas.setText(String.format("Total Vendido: R$ %.2f", totalVendido));
        }
        if (lblTotalLucro != null) {
            lblTotalLucro.setText(String.format("Lucro Total: R$ %.2f", totalLucro));
        }
    }
}
