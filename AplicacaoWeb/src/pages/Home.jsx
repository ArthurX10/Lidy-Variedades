import Header from '../components/Header';
import BannerPromocional from '../components/BannerPromocional';
import Footer from '../components/Footer';
import Categorias from '../components/Categorias';
import Destaques from '../components/Destaques';
import BannerOferta from '../components/BannerOferta';
import Acervo from '../components/Acervo';

const produtosVazios = [];

const Home = ({ produtos = produtosVazios, carregando = false }) => {
  return (
    <>
      <Header />
      <main>
        <BannerPromocional />
        <Categorias />
        <Destaques produtos={produtos} carregando={carregando} />
        <BannerOferta />
        <Acervo produtos={produtos} carregando={carregando} />
      </main>
      <Footer />
    </>
  );
};

export default Home;
