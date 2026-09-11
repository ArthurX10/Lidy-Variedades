package org.example.lidyvariedadesdesktop;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import org.example.lidyvariedadesdesktop.service.ProdutoService;

import java.math.BigDecimal;
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

    private final ProdutoService produtoService = new ProdutoService();
    private final ObservableList<Produto> listaProdutos = FXCollections.observableArrayList();

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

        // Associa a lista observável à tabela
        tabelaProdutos.setItems(listaProdutos);

        // 2. Busca os produtos ao abrir a tela
        onCarregarProdutos();
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
}
