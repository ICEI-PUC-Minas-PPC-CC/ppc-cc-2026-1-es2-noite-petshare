const api = "/api";

const panelLabels = {
  home: "Inicio",
  perfil: "Perfil",
  pets: "Pets",
  cuidadores: "Busca de Cuidadores",
  solicitacoes: "Pedidos",
  notificacoes: "Notificacoes",
};

const state = {
  view: "landing",
  authTab: "login",
  panel: "home",
  usuarioLogado: null,
  petsCache: [],
  cuidadoresCache: [],
  solicitacoesCache: [],
  notificacoesCache: [],
  pedidosTab: "ativos",
  chatSolicitacaoId: null,
  chatTimer: null,
};

const $ = (sel) => document.querySelector(sel);
const $$ = (sel) => Array.from(document.querySelectorAll(sel));

function show(el, visible = true) {
  if (!el) return;
  el.classList.toggle("hidden", !visible);
}

function showToast(message, type = "info") {
  const region = $("#toast-region");
  if (!region || !message) return;

  const toast = document.createElement("div");
  toast.className = `toast toast-${type}`;
  toast.textContent = message;
  region.appendChild(toast);

  window.setTimeout(() => {
    toast.remove();
  }, 2800);
}

function renderSkeleton(container, count = 3, tag = "div") {
  if (!container) return;
  container.innerHTML = "";
  for (let i = 0; i < count; i++) {
    const sk = document.createElement(tag);
    sk.className = "skeleton-card";
    sk.innerHTML = `<span class="sk-line w60"></span><span class="sk-line w90"></span><span class="sk-line w40"></span>`;
    container.appendChild(sk);
  }
}

function setFeedback(msg, erro = false) {
  const el = $("#feedback");
  if (!el) return;

  el.textContent = msg;
  el.style.color = erro ? "#c53030" : "#1f9e5f";

  if (msg) {
    showToast(msg, erro ? "error" : "success");
  }
}

function setGlobalLoading(visible, text = "Carregando...") {
  const root = $("#global-loading");
  if (!root) return;

  show(root, visible);

  const txt = $("#global-loading-text");
  if (txt) {
    txt.textContent = text;
  }
}

function setButtonBusy(button, busy, busyText = "Processando...") {
  if (!button) return;

  if (busy) {
    button.dataset.originalText = button.textContent;
    button.textContent = busyText;
    button.disabled = true;
    return;
  }

  button.textContent = button.dataset.originalText || button.textContent;
  button.disabled = false;
}

function ensureFormValidity(form) {
  if (form.checkValidity()) {
    return true;
  }
  form.reportValidity();
  return false;
}

function usuarioResumo(u) {
  return `${u.nome} (${u.tipo}) | ${u.email}`;
}

function renderBreadcrumb() {
  const current = $("#breadcrumb-current");
  if (!current) return;
  current.textContent = panelLabels[state.panel] || "Perfil";
}

function updateHash() {
  const hashByView = {
    landing: "#home",
    auth: state.authTab === "register" ? "#cadastrar" : "#login",
    dashboard: "#dashboard",
  };
  window.location.hash = hashByView[state.view];
}

function renderView() {
  show($("#view-landing"), state.view === "landing");
  show($("#view-auth"), state.view === "auth");
  show($("#view-dashboard"), state.view === "dashboard");

  const appShellVisible = state.view === "dashboard";
  show($("#dashboard-topbar"), appShellVisible);
  show($("#dashboard-bottom-nav"), appShellVisible);

  renderBreadcrumb();
  updateHash();
}

function setAuthTab(tab) {
  state.authTab = tab;
  $$(".tab-btn").forEach((btn) => btn.classList.toggle("active", btn.dataset.authTab === tab));
  show($("#login-form"), tab === "login");
  show($("#cadastro-form"), tab === "register");
  $("#auth-title").textContent = tab === "register" ? "Criar conta" : "Acessar";
  updateHash();
}

function setPanel(panel) {
  state.panel = panel;
  $$(".panel").forEach((el) => el.classList.toggle("hidden", el.dataset.panel !== panel));
  $$("[data-panel-target]").forEach((item) => item.classList.toggle("active", item.dataset.panelTarget === panel));
  renderBreadcrumb();
}

function preencherPerfilForm(u) {
  const form = $("#perfil-form");
  form.nome.value = u.nome || "";
  form.telefone.value = u.telefone || "";
  form.descricao.value = u.descricao || "";
  form.disponibilidade.value = u.disponibilidade || "";
  form.fotoUrl.value = u.fotoUrl || "";
}

function renderDashboardByTipo() {
  const isDono = state.usuarioLogado?.tipo === "DONO";
  show($("#pets-section"), isDono);
  show($("#nav-pets"), isDono);
  show($("#home-busca-cta"), isDono);

  if (!isDono && state.panel === "pets") {
    setPanel("home");
  }
}

async function req(url, options = {}) {
  const { loadingMessage, silent, ...fetchOptions } = options;

  if (!silent && loadingMessage) {
    setGlobalLoading(true, loadingMessage);
  }

  try {
    const response = await fetch(url, {
      headers: { "Content-Type": "application/json" },
      ...fetchOptions,
    });

    if (!response.ok) {
      const texto = await response.text();
      throw new Error(texto || "Falha na requisicao");
    }

    const contentType = response.headers.get("content-type") || "";
    return contentType.includes("application/json") ? response.json() : null;
  } finally {
    if (!silent && loadingMessage) {
      setGlobalLoading(false);
    }
  }
}

async function carregarPets() {
  if (!state.usuarioLogado || state.usuarioLogado.tipo !== "DONO") return;

  renderSkeleton($("#lista-pets"), 3, "li");
  state.petsCache = await req(`${api}/usuarios/${state.usuarioLogado.id}/pets`, { silent: true });

  const ul = $("#lista-pets");
  ul.innerHTML = "";

  const sel = $("#sol-pet");
  sel.innerHTML = "";

  state.petsCache.forEach((pet) => {
    const li = document.createElement("li");
    li.textContent = `${pet.nome} - ${pet.especie}${pet.raca ? ` (${pet.raca})` : ""}`;
    ul.appendChild(li);

    const op = document.createElement("option");
    op.value = pet.id;
    op.textContent = `${pet.nome} (${pet.especie})`;
    sel.appendChild(op);
  });

  if (state.petsCache.length === 0) {
    const empty = document.createElement("li");
    empty.className = "empty-state";
    empty.textContent = "Nenhum pet cadastrado ainda.";
    ul.appendChild(empty);
  }
}

function aplicaFiltrosCuidadores(lista) {
  const termo = ($("#filtro-busca").value || "").toLowerCase();

  return lista.filter((c) => {
    const blob = `${c.nome} ${c.descricao || ""} ${c.disponibilidade || ""}`.toLowerCase();
    return blob.includes(termo);
  });
}

function renderCuidadores() {
  const wrap = $("#lista-cuidadores");
  wrap.innerHTML = "";

  const sel = $("#sol-cuidador");
  sel.innerHTML = "";

  const filtrados = aplicaFiltrosCuidadores(state.cuidadoresCache);

  if (filtrados.length === 0) {
    const empty = document.createElement("div");
    empty.className = "empty-state-card";
    empty.textContent = "Nenhum cuidador encontrado para esse filtro.";
    wrap.appendChild(empty);
  }

  const isDono = state.usuarioLogado?.tipo === "DONO";

  filtrados.forEach((c) => {
    const div = document.createElement("div");
    div.className = "cuidador-card";
    div.innerHTML = `
      <strong>${c.nome}</strong>
      <div>${c.descricao || "Sem descricao"}</div>
      <small>Disponibilidade: ${c.disponibilidade || "Nao informada"}</small>
    `;

    if (isDono) {
      div.classList.add("clickable");
      div.setAttribute("role", "button");
      div.setAttribute("tabindex", "0");

      const cta = document.createElement("button");
      cta.type = "button";
      cta.className = "btn btn-primary";
      cta.style.marginTop = "10px";
      cta.textContent = "Solicitar cuidado";
      div.appendChild(cta);

      const abrir = () => iniciarSolicitacao(c.id);
      div.addEventListener("click", abrir);
      div.addEventListener("keydown", (e) => {
        if (e.key === "Enter" || e.key === " ") {
          e.preventDefault();
          abrir();
        }
      });
    }

    wrap.appendChild(div);

    const op = document.createElement("option");
    op.value = c.id;
    op.textContent = c.nome;
    sel.appendChild(op);
  });
}

function iniciarSolicitacao(cuidadorId) {
  if (state.usuarioLogado?.tipo !== "DONO") return;

  const sel = $("#sol-cuidador");
  if (sel) sel.value = String(cuidadorId);

  const cuidador = state.cuidadoresCache.find((c) => String(c.id) === String(cuidadorId));
  const nome = $("#sol-cuidador-nome");
  if (nome) nome.textContent = cuidador ? `Cuidador: ${cuidador.nome}` : "";

  openSheet("sheet-solicitacao");
}

function openSheet(id) {
  show($("#" + id), true);
  document.body.style.overflow = "hidden";
}

function closeSheet(id) {
  show($("#" + id), false);
  document.body.style.overflow = "";
  if (id === "sheet-chat" && state.chatTimer) {
    clearInterval(state.chatTimer);
    state.chatTimer = null;
    state.chatSolicitacaoId = null;
  }
}

async function carregarCuidadores() {
  renderSkeleton($("#lista-cuidadores"), 4);
  state.cuidadoresCache = await req(`${api}/cuidadores`, { silent: true });
  renderCuidadores();
}

async function carregarSolicitacoes() {
  if (!state.usuarioLogado) return;

  renderSkeleton($("#lista-solicitacoes"), 3);
  const lista = await req(`${api}/solicitacoes?usuarioId=${state.usuarioLogado.id}`, { silent: true });
  state.solicitacoesCache = lista || [];
  renderHome();
  renderSolicitacoes();
}

const statusPorTab = {
  ativos: ["PENDENTE"],
  andamento: ["ACEITA"],
  finalizados: ["RECUSADA", "CANCELADA"],
};

function renderSolicitacoes() {
  const wrap = $("#lista-solicitacoes");
  if (!wrap) return;
  wrap.innerHTML = "";

  const permitidos = statusPorTab[state.pedidosTab] || [];
  const lista = (state.solicitacoesCache || []).filter((s) => permitidos.includes(s.status));

  if (lista.length === 0) {
    const empty = document.createElement("div");
    empty.className = "empty-state-card";
    empty.textContent = "Nenhum pedido nesta categoria.";
    wrap.appendChild(empty);
    return;
  }

  lista.forEach((s) => {
    const card = document.createElement("div");
    card.className = "solicitacao-card";
    card.innerHTML = `
      <strong>#${s.id} - ${s.pet.nome}</strong>
      <div>Dono: ${s.dono.nome}</div>
      <div>Cuidador: ${s.cuidador.nome}</div>
      <div>Periodo: ${s.dataInicio} ate ${s.dataFim}</div>
      <div>Status: <span class="status ${s.status}">${s.status}</span></div>
      <small>${s.observacoes || "Sem observacoes"}</small>
    `;

    const acoes = document.createElement("div");
    acoes.className = "card-actions";

    if (state.usuarioLogado.tipo === "CUIDADOR" && s.cuidador.id === state.usuarioLogado.id && s.status === "PENDENTE") {
      const aceitar = document.createElement("button");
      aceitar.className = "btn btn-primary";
      aceitar.textContent = "Aceitar";
      aceitar.onclick = () => atualizarStatusSolicitacao(s.id, "ACEITA");

      const recusar = document.createElement("button");
      recusar.className = "btn btn-secondary";
      recusar.textContent = "Recusar";
      recusar.onclick = () => atualizarStatusSolicitacao(s.id, "RECUSADA");

      acoes.appendChild(aceitar);
      acoes.appendChild(recusar);
    }

    if (s.status === "ACEITA") {
      const chat = document.createElement("button");
      chat.className = "btn btn-ghost";
      chat.textContent = "Abrir chat";
      chat.onclick = () => abrirChat(s.id, s.pet.nome);
      acoes.appendChild(chat);
    }

    if (acoes.children.length) card.appendChild(acoes);
    wrap.appendChild(card);
  });
}

async function atualizarStatusSolicitacao(id, status) {
  try {
    await req(`${api}/solicitacoes/${id}/status`, {
      method: "PATCH",
      body: JSON.stringify({ cuidadorId: state.usuarioLogado.id, status }),
      loadingMessage: "Atualizando status...",
    });
    await Promise.all([carregarSolicitacoes(), carregarNotificacoes()]);
    setFeedback(`Solicitacao ${status.toLowerCase()} com sucesso`);
  } catch (err) {
    setFeedback(`Erro ao atualizar solicitacao: ${err.message}`, true);
  }
}

async function carregarNotificacoes() {
  if (!state.usuarioLogado) return;

  renderSkeleton($("#lista-notificacoes"), 3, "li");
  const lista = await req(`${api}/usuarios/${state.usuarioLogado.id}/notificacoes`, { silent: true });
  state.notificacoesCache = lista || [];
  renderHome();
  const ul = $("#lista-notificacoes");
  ul.innerHTML = "";

  if (lista.length === 0) {
    const empty = document.createElement("li");
    empty.className = "empty-state";
    empty.textContent = "Voce nao possui notificacoes no momento.";
    ul.appendChild(empty);
    return;
  }

  lista.forEach((n) => {
    const li = document.createElement("li");
    li.textContent = `${n.dataHora} - ${n.mensagem} ${n.lida ? "" : "(nao lida)"}`;

    if (!n.lida) {
      const btn = document.createElement("button");
      btn.className = "btn btn-ghost";
      btn.textContent = "Marcar lida";
      btn.style.marginLeft = "10px";
      btn.onclick = async () => {
        await req(`${api}/notificacoes/${n.id}/lida`, { method: "PATCH", loadingMessage: "Atualizando notificacao..." });
        await carregarNotificacoes();
      };
      li.appendChild(btn);
    }

    ul.appendChild(li);
  });
}

async function abrirChat(solicitacaoId, petNome) {
  state.chatSolicitacaoId = solicitacaoId;
  const title = $("#sheet-chat-title");
  if (title) title.textContent = `Chat - ${petNome}`;
  const wrap = $("#chat-mensagens");
  if (wrap) { wrap.dataset.count = ""; wrap.innerHTML = ""; }
  openSheet("sheet-chat");
  await carregarMensagens();
  if (state.chatTimer) clearInterval(state.chatTimer);
  state.chatTimer = setInterval(() => carregarMensagens(), 4000);
}

async function carregarMensagens() {
  if (!state.chatSolicitacaoId) return;
  const wrap = $("#chat-mensagens");
  const lista = await req(`${api}/solicitacoes/${state.chatSolicitacaoId}/mensagens`, { silent: true });

  if (wrap.dataset.count === String((lista || []).length)) return;

  const nearBottom = wrap.scrollHeight - wrap.scrollTop - wrap.clientHeight < 80;
  wrap.dataset.count = String((lista || []).length);
  wrap.innerHTML = "";

  if (!lista || lista.length === 0) {
    wrap.innerHTML = `<div class="empty-state-card">Sem mensagens ainda. Diga ola!</div>`;
    return;
  }

  lista.forEach((m) => {
    const div = document.createElement("div");
    const mine = m.remetente.id === state.usuarioLogado.id;
    div.className = `chat-bubble ${mine ? "mine" : "theirs"}`;
    div.innerHTML = `<span>${m.conteudo}</span><small>${m.remetente.nome}</small>`;
    wrap.appendChild(div);
  });
  if (nearBottom) wrap.scrollTop = wrap.scrollHeight;
}

async function enviarMensagem(conteudo) {
  if (!state.chatSolicitacaoId || !conteudo.trim()) return;
  await req(`${api}/solicitacoes/${state.chatSolicitacaoId}/mensagens`, {
    method: "POST",
    body: JSON.stringify({ remetenteId: state.usuarioLogado.id, conteudo: conteudo.trim() }),
    silent: true,
  });
  await carregarMensagens();
  const wrap = $("#chat-mensagens");
  if (wrap) wrap.scrollTop = wrap.scrollHeight;
}

function entrarNoDashboard(usuario) {
  state.usuarioLogado = usuario;
  $("#usuario-info").textContent = usuarioResumo(usuario);
  preencherPerfilForm(usuario);
  renderDashboardByTipo();
  state.view = "dashboard";
  setPanel("home");
  renderHome();
  renderView();
}

function primeiroNome(u) {
  return (u?.nome || "").split(" ")[0] || "tutor";
}

function renderHome() {
  if (!state.usuarioLogado) return;

  const saud = $("#home-saudacao");
  if (saud) saud.textContent = `Ola, ${primeiroNome(state.usuarioLogado)}!`;

  const sols = state.solicitacoesCache || [];
  const isDono = state.usuarioLogado.tipo === "DONO";
  const pend = sols.filter((s) => s.status === "PENDENTE").length;
  const ace = sols.filter((s) => s.status === "ACEITA").length;
  const naoLidas = (state.notificacoesCache || []).filter((n) => !n.lida).length;

  const stats = [
    { label: isDono ? "Pets" : "Cuidadores", value: isDono ? state.petsCache.length : state.cuidadoresCache.length, panel: isDono ? "pets" : "cuidadores" },
    { label: "Pendentes", value: pend, panel: "solicitacoes" },
    { label: "Aceitas", value: ace, panel: "solicitacoes" },
    { label: "Avisos", value: naoLidas, panel: "notificacoes" },
  ];

  const grid = $("#home-stats");
  if (grid) {
    grid.innerHTML = "";
    stats.forEach((s) => {
      const c = document.createElement("button");
      c.type = "button";
      c.className = "stat-card";
      c.innerHTML = `<span class="stat-value">${s.value}</span><span class="stat-label">${s.label}</span>`;
      c.addEventListener("click", () => setPanel(s.panel));
      grid.appendChild(c);
    });
  }

  const rec = $("#home-recentes");
  if (rec) {
    rec.innerHTML = "";
    const ultimas = [...sols].slice(-3).reverse();
    if (ultimas.length === 0) {
      rec.innerHTML = `<div class="empty-state-card">Sem solicitacoes ainda. ${isDono ? "Busque um cuidador para comecar." : "Aguarde novos pedidos."}</div>`;
    } else {
      ultimas.forEach((s) => {
        const card = document.createElement("button");
        card.type = "button";
        card.className = "home-recent-card";
        card.innerHTML = `<strong>${s.pet.nome}</strong><span class="status ${s.status}">${s.status}</span><small>${s.dataInicio} ate ${s.dataFim}</small>`;
        card.addEventListener("click", () => setPanel("solicitacoes"));
        rec.appendChild(card);
      });
    }
  }
}

async function carregarDadosIniciaisDashboard() {
  setGlobalLoading(true, "Carregando dados do dashboard...");
  try {
    await Promise.all([carregarCuidadores(), carregarPets(), carregarSolicitacoes(), carregarNotificacoes()]);
  } finally {
    setGlobalLoading(false);
  }
}

function sair() {
  state.usuarioLogado = null;
  state.petsCache = [];
  state.cuidadoresCache = [];
  state.view = "landing";
  renderView();
  setFeedback("Sessao encerrada");
}

function bindEvents() {
  $$("[data-go-auth]").forEach((btn) => {
    btn.addEventListener("click", () => {
      state.view = "auth";
      setAuthTab(btn.dataset.goAuth === "register" ? "register" : "login");
      renderView();
    });
  });

  $("#btn-back-landing").addEventListener("click", () => {
    state.view = "landing";
    renderView();
  });

  $$(".tab-btn").forEach((btn) => btn.addEventListener("click", () => setAuthTab(btn.dataset.authTab)));
  $$("[data-panel-target]").forEach((btn) => btn.addEventListener("click", () => setPanel(btn.dataset.panelTarget)));

  $("#btn-logout").addEventListener("click", sair);
  $("#btn-open-notifications").addEventListener("click", () => setPanel("notificacoes"));

  ["#filtro-busca", "#filtro-raio", "#filtro-valor", "#filtro-avaliacao"].forEach((sel) => {
    const el = $(sel);
    el?.addEventListener("input", renderCuidadores);
    el?.addEventListener("change", renderCuidadores);
  });

  $("#cadastro-form").addEventListener("submit", async (e) => {
    e.preventDefault();
    if (!ensureFormValidity(e.target)) return;

    const submit = e.submitter || e.target.querySelector('button[type="submit"]');
    setButtonBusy(submit, true, "Criando conta...");

    const data = Object.fromEntries(new FormData(e.target).entries());

    if (data.senha !== data.confirmarSenha) {
      setFeedback("Confirmacao de senha invalida", true);
      setButtonBusy(submit, false);
      return;
    }

    delete data.confirmarSenha;

    try {
      const usuario = await req(`${api}/usuarios/cadastro`, {
        method: "POST",
        body: JSON.stringify(data),
        loadingMessage: "Criando conta...",
      });
      setFeedback(`Conta criada com id ${usuario.id}. Agora faca seu login.`);
      e.target.reset();
      setAuthTab("login");
    } catch (err) {
      setFeedback(`Erro no cadastro: ${err.message}`, true);
    } finally {
      setButtonBusy(submit, false);
    }
  });

  $("#login-form").addEventListener("submit", async (e) => {
    e.preventDefault();
    if (!ensureFormValidity(e.target)) return;

    const submit = e.submitter || e.target.querySelector('button[type="submit"]');
    setButtonBusy(submit, true, "Entrando...");

    const data = Object.fromEntries(new FormData(e.target).entries());

    try {
      const usuario = await req(`${api}/usuarios/login`, {
        method: "POST",
        body: JSON.stringify(data),
        loadingMessage: "Validando acesso...",
      });
      entrarNoDashboard(usuario);
      await carregarDadosIniciaisDashboard();
      setFeedback("Login realizado com sucesso");
    } catch (err) {
      setFeedback(`Erro no login: ${err.message}`, true);
    } finally {
      setButtonBusy(submit, false);
    }
  });

  $("#perfil-form").addEventListener("submit", async (e) => {
    e.preventDefault();
    if (!state.usuarioLogado) return;

    const submit = e.submitter || e.target.querySelector('button[type="submit"]');
    setButtonBusy(submit, true, "Salvando...");

    const data = Object.fromEntries(new FormData(e.target).entries());

    try {
      state.usuarioLogado = await req(`${api}/usuarios/${state.usuarioLogado.id}`, {
        method: "PUT",
        body: JSON.stringify(data),
        loadingMessage: "Atualizando perfil...",
      });
      $("#usuario-info").textContent = usuarioResumo(state.usuarioLogado);
      setFeedback("Perfil atualizado");
    } catch (err) {
      setFeedback(`Erro ao atualizar perfil: ${err.message}`, true);
    } finally {
      setButtonBusy(submit, false);
    }
  });

  $("#pet-form").addEventListener("submit", async (e) => {
    e.preventDefault();
    if (!state.usuarioLogado || state.usuarioLogado.tipo !== "DONO") return;
    if (!ensureFormValidity(e.target)) return;

    const submit = e.submitter || e.target.querySelector('button[type="submit"]');
    setButtonBusy(submit, true, "Salvando pet...");

    const data = Object.fromEntries(new FormData(e.target).entries());
    data.donoId = state.usuarioLogado.id;
    if (data.idade) data.idade = Number(data.idade);

    try {
      await req(`${api}/pets`, {
        method: "POST",
        body: JSON.stringify(data),
        loadingMessage: "Cadastrando pet...",
      });
      await carregarPets();
      e.target.reset();
      setFeedback("Pet cadastrado com sucesso");
    } catch (err) {
      setFeedback(`Erro ao cadastrar pet: ${err.message}`, true);
    } finally {
      setButtonBusy(submit, false);
    }
  });

  $("#btn-carregar-cuidadores").addEventListener("click", async () => {
    const button = $("#btn-carregar-cuidadores");
    setButtonBusy(button, true, "Atualizando...");
    try {
      await carregarCuidadores();
      setFeedback("Lista de cuidadores atualizada");
    } catch (err) {
      setFeedback(`Erro ao carregar cuidadores: ${err.message}`, true);
    } finally {
      setButtonBusy(button, false);
    }
  });

  $("#solicitacao-form").addEventListener("submit", async (e) => {
    e.preventDefault();
    if (!state.usuarioLogado || state.usuarioLogado.tipo !== "DONO") return;
    if (!ensureFormValidity(e.target)) return;

    const submit = e.submitter || e.target.querySelector('button[type="submit"]');
    setButtonBusy(submit, true, "Enviando solicitacao...");

    const data = Object.fromEntries(new FormData(e.target).entries());
    data.donoId = state.usuarioLogado.id;
    data.petId = Number(data.petId);
    data.cuidadorId = Number(data.cuidadorId);

    if (data.hora) {
      data.observacoes = `${data.observacoes || ""} [Hora: ${data.hora}]`.trim();
    }

    delete data.hora;

    try {
      await req(`${api}/solicitacoes`, {
        method: "POST",
        body: JSON.stringify(data),
        loadingMessage: "Registrando solicitacao...",
      });
      await Promise.all([carregarSolicitacoes(), carregarNotificacoes()]);
      setFeedback("Solicitacao criada com sucesso");
      e.target.reset();
      closeSheet("sheet-solicitacao");
      setPanel("solicitacoes");
    } catch (err) {
      setFeedback(`Erro ao criar solicitacao: ${err.message}`, true);
    } finally {
      setButtonBusy(submit, false);
    }
  });

  $("#btn-carregar-notificacoes").addEventListener("click", async () => {
    const button = $("#btn-carregar-notificacoes");
    setButtonBusy(button, true, "Atualizando...");
    try {
      await carregarNotificacoes();
      setFeedback("Notificacoes atualizadas");
    } catch (err) {
      setFeedback(`Erro ao carregar notificacoes: ${err.message}`, true);
    } finally {
      setButtonBusy(button, false);
    }
  });

  $("#btn-carregar-solicitacoes").addEventListener("click", async () => {
    const button = $("#btn-carregar-solicitacoes");
    setButtonBusy(button, true, "Atualizando...");
    try {
      await carregarSolicitacoes();
      setFeedback("Pedidos atualizados");
    } catch (err) {
      setFeedback(`Erro ao carregar pedidos: ${err.message}`, true);
    } finally {
      setButtonBusy(button, false);
    }
  });

  $$("[data-status-tab]").forEach((btn) => {
    btn.addEventListener("click", () => {
      state.pedidosTab = btn.dataset.statusTab;
      $$("[data-status-tab]").forEach((b) => b.classList.toggle("active", b === btn));
      renderSolicitacoes();
    });
  });

  $$("[data-close-sheet]").forEach((btn) => {
    btn.addEventListener("click", () => closeSheet(btn.dataset.closeSheet));
  });

  $$(".sheet-backdrop").forEach((bg) => {
    bg.addEventListener("click", (e) => {
      if (e.target === bg) closeSheet(bg.id);
    });
  });

  $("#chat-form").addEventListener("submit", async (e) => {
    e.preventDefault();
    const input = $("#chat-input");
    const txt = input.value;
    input.value = "";
    try {
      await enviarMensagem(txt);
    } catch (err) {
      setFeedback(`Erro ao enviar mensagem: ${err.message}`, true);
    }
  });

  bindPullToRefresh();
}

function bindPullToRefresh() {
  const main = $("#main-content");
  const ptr = $("#ptr");
  if (!main || !ptr) return;

  let startY = 0;
  let pulling = false;
  const threshold = 70;

  main.addEventListener("touchstart", (e) => {
    if (state.view !== "dashboard" || window.scrollY > 0) return;
    startY = e.touches[0].clientY;
    pulling = true;
  }, { passive: true });

  main.addEventListener("touchmove", (e) => {
    if (!pulling) return;
    const dist = e.touches[0].clientY - startY;
    if (dist > 0) {
      ptr.style.transform = `translateY(${Math.min(dist, threshold)}px)`;
      ptr.classList.toggle("ready", dist >= threshold);
    }
  }, { passive: true });

  main.addEventListener("touchend", async (e) => {
    if (!pulling) return;
    const dist = e.changedTouches[0].clientY - startY;
    pulling = false;
    if (dist >= threshold) {
      ptr.classList.add("loading");
      try { await carregarDadosIniciaisDashboard(); } finally { ptr.classList.remove("loading", "ready"); }
    }
    ptr.style.transform = "";
  });
}

function initByHash() {
  const hash = window.location.hash;
  if (hash === "#login") {
    state.view = "auth";
    state.authTab = "login";
  } else if (hash === "#cadastrar") {
    state.view = "auth";
    state.authTab = "register";
  } else {
    state.view = "landing";
  }
}

initByHash();
setAuthTab(state.authTab);
bindEvents();
setPanel("home");
renderView();
