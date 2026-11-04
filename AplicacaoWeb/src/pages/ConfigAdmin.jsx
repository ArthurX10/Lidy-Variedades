import { useState } from 'react';
import Header from '../components/Header';
import Footer from '../components/Footer';
import AdminSidebar from '../components/AdminSidebar';
import { FaCheck, FaWhatsapp } from 'react-icons/fa';
import { FaRegFlag } from 'react-icons/fa6';
import './ConfigAdmin.css';
import { useStoreSettings } from '../hooks/useStoreSettings';

const ConfigAdmin = () => {
  const { settings, loading, saveSettings } = useStoreSettings();
  const [formEdicao, setFormEdicao] = useState(null);
  const [salvando, setSalvando] = useState(false);
  const [erro, setErro] = useState('');
  const [sucesso, setSucesso] = useState('');

  const form = formEdicao ?? settings;

  const alterarCampo = (event) => {
    const { name, value, checked, type } = event.target;
    setFormEdicao((atual) => ({
      ...(atual ?? settings),
      [name]: type === 'checkbox' ? checked : value,
    }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setErro('');
    setSucesso('');
    setSalvando(true);
    try {
      await saveSettings({
        whatsappNumber: form.whatsappNumber.replace(/\D/g, ''),
        bannerEnabled: form.bannerEnabled,
        bannerText: form.bannerText.trim(),
        instagramProfile: form.instagramProfile.trim(),
        instagramUrl: form.instagramUrl.trim(),
      });
      setSucesso('Configurações salvas com sucesso.');
    } catch (requestError) {
      setErro(requestError.message || 'Não foi possível salvar as configurações.');
    } finally {
      setSalvando(false);
    }
  };

  return (
    <div className="config-page">
      <Header />
      <main className="config-dashboard">
        <AdminSidebar />
        <form className="config-container" onSubmit={handleSubmit}>
          <div className="config-heading">
            <div>
              <h1>Configurações Gerais</h1>
              <p>Ajuste as configurações da loja</p>
            </div>
          </div>

          <fieldset className="config-form-fields" disabled={loading || salvando}>
            <div className="config-sections">
              <section className="config-card" aria-labelledby="config-whatsapp-title">
                  <div className="config-card-heading">
                    <FaWhatsapp aria-hidden="true" />
                    <div>
                      <h2 id="config-whatsapp-title">Atendimento via WhatsApp</h2>
                      <p>Defina o número usado no atendimento e nos pedidos.</p>
                    </div>
                  </div>
                  <div className="config-field">
                    <label htmlFor="whatsapp-number">Número do WhatsApp</label>
                    <input
                      id="whatsapp-number"
                      name="whatsappNumber"
                      type="tel"
                      inputMode="numeric"
                      pattern="[0-9+() -]{10,20}"
                      required
                      placeholder="Ex.: 5588999999999"
                      autoComplete="tel"
                      value={form.whatsappNumber}
                      onChange={alterarCampo}
                    />
                    <small>Apenas números, incluindo o código do país e o DDD.</small>
                  </div>
              </section>

              <section className="config-card" aria-labelledby="config-banner-title">
                  <div className="config-card-heading">
                    <FaRegFlag aria-hidden="true" />
                    <div>
                      <h2 id="config-banner-title">Banner de aviso</h2>
                      <p>Configure a mensagem exibida no topo da loja.</p>
                    </div>
                </div>
                  <label className="config-toggle" htmlFor="banner-enabled">
                    <input
                      id="banner-enabled"
                      name="bannerEnabled"
                      type="checkbox"
                      checked={form.bannerEnabled}
                      onChange={alterarCampo}
                    />
                    <span>Mostrar banner no topo da página</span>
                  </label>
                  <div className="config-field">
                    <label htmlFor="banner-text">Texto do banner</label>
                    <input
                      id="banner-text"
                      name="bannerText"
                      type="text"
                      placeholder="Ex.: Frete grátis nas compras acima de R$ 200,00"
                      value={form.bannerText}
                      onChange={alterarCampo}
                    />
                  </div>
              </section>

              <section className="config-card" aria-labelledby="config-social-title">
                  <div className="config-card-heading">
                    <div>
                      <h2 id="config-social-title">Redes sociais</h2>
                      <p>Adicione o perfil da loja para facilitar o acesso dos clientes.</p>
                    </div>
                </div>
                  <div className="config-social-fields">
                    <div className="config-field">
                      <label htmlFor="instagram-profile">Perfil do Instagram</label>
                      <input
                        id="instagram-profile"
                        name="instagramProfile"
                        type="text"
                        autoComplete="off"
                        placeholder="@sualoja"
                        value={form.instagramProfile}
                        onChange={alterarCampo}
                      />
                    </div>
                    <div className="config-field">
                      <label htmlFor="instagram-url">Link do Instagram</label>
                      <input
                        id="instagram-url"
                        name="instagramUrl"
                        type="url"
                        placeholder="https://www.instagram.com/sualoja/"
                        value={form.instagramUrl}
                        onChange={alterarCampo}
                      />
                    </div>
                </div>
              </section>
            </div>
          </fieldset>

          {erro && <p className="config-form-message config-form-error" role="alert">{erro}</p>}
          {sucesso && <p className="config-form-message config-form-success" role="status">{sucesso}</p>}
          <button className="config-save-button" type="submit" disabled={loading || salvando}>
            <FaCheck aria-hidden="true" />
            {salvando ? 'Salvando...' : 'Salvar alterações'}
          </button>
        </form>
      </main>
      <Footer />
    </div>
  );
};

export default ConfigAdmin;
