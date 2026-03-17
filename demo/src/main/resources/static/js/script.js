/* ─── CSRF TOKEN ─────────────────────────── */
function getCsrfToken() {
  const meta = document.querySelector('meta[name="_csrf"]');
  const header = document.querySelector('meta[name="_csrf_header"]');
  if (!meta || !header) return null;
  return { header: header.content, token: meta.content };
}

function csrfHeaders(extra = {}) {
  const csrf = getCsrfToken();
  if (!csrf) return extra;
  return { ...extra, [csrf.header]: csrf.token };
}

/* ─── COMMENT WRITE ─────────────────────── */
async function submitComment(postId) {
  const content = document.getElementById('comment-content').value.trim();
  if (!content) { showAlert('댓글 내용을 입력해주세요.', 'danger'); return; }

  const res = await fetch('/api/comment', {
    method: 'POST',
    headers: csrfHeaders({ 'Content-Type': 'application/json' }),
    body: JSON.stringify({ postId, content })
  });

  const data = await res.json();
  if (data.success) {
    location.reload();
  } else {
    showAlert(data.message || '댓글 작성에 실패했습니다.', 'danger');
  }
}

/* ─── COMMENT DELETE ────────────────────── */
async function deleteComment(commentId) {
  if (!confirm('댓글을 삭제하시겠습니까?')) return;

  const res = await fetch(`/api/comment/${commentId}`, { method: 'DELETE', headers: csrfHeaders() });
  const data = await res.json();

  if (data.success) {
    const el = document.getElementById(`comment-${commentId}`);
    if (el) {
      el.style.opacity = '0';
      el.style.transform = 'translateY(-8px)';
      el.style.transition = 'all 0.3s ease';
      setTimeout(() => el.remove(), 300);
    }
  } else {
    showAlert(data.message || '삭제에 실패했습니다.', 'danger');
  }
}

/* ─── POST DELETE ───────────────────────── */
function deletePost(postId) {
  if (!confirm('게시글을 삭제하시겠습니까?\n이 작업은 되돌릴 수 없습니다.')) return;
  const csrf = getCsrfToken();
  const form = document.createElement('form');
  form.method = 'POST';
  form.action = `/admin/delete/${postId}`;
  if (csrf) {
    const input = document.createElement('input');
    input.type  = 'hidden';
    input.name  = '_csrf';
    input.value = csrf.token;
    form.appendChild(input);
  }
  document.body.appendChild(form);
  form.submit();
}

/* ─── IMAGE PREVIEW ─────────────────────── */
function setupImageUpload() {
  const zone    = document.querySelector('.image-upload-zone');
  const input   = document.getElementById('image-input');
  const preview = document.getElementById('image-preview');
  if (!zone || !input) return;

  input.addEventListener('change', e => {
    const file = e.target.files[0];
    if (!file) return;
    const reader = new FileReader();
    reader.onload = ev => {
      preview.src = ev.target.result;
      preview.style.display = 'block';
      zone.querySelector('.image-upload-icon').style.display = 'none';
      zone.querySelector('.image-upload-text').style.display = 'none';
    };
    reader.readAsDataURL(file);
  });

  zone.addEventListener('dragover', e => { e.preventDefault(); zone.classList.add('drag-over'); });
  zone.addEventListener('dragleave', () => zone.classList.remove('drag-over'));
  zone.addEventListener('drop', e => {
    e.preventDefault();
    zone.classList.remove('drag-over');
    const file = e.dataTransfer.files[0];
    if (file && file.type.startsWith('image/')) {
      const dt = new DataTransfer();
      dt.items.add(file);
      input.files = dt.files;
      input.dispatchEvent(new Event('change'));
    }
  });
}

/* ─── CATEGORY TABS ─────────────────────── */
function setupCategoryTabs() {
  document.querySelectorAll('.category-tab').forEach(tab => {
    tab.addEventListener('click', () => {
      const cat = tab.dataset.category;
      const url = new URL(location.href);
      if (cat) {
        url.searchParams.set('category', cat);
        url.searchParams.delete('keyword');
      } else {
        url.searchParams.delete('category');
        url.searchParams.delete('keyword');
      }
      url.searchParams.set('page', '0');
      location.href = url.toString();
    });
  });
}

/* ─── SEARCH ────────────────────────────── */
function setupSearch() {
  const form = document.getElementById('search-form');
  if (!form) return;
  form.addEventListener('submit', e => {
    e.preventDefault();
    const kw = document.getElementById('search-input').value.trim();
    if (!kw) return;
    const url = new URL('/post/list', location.origin);
    url.searchParams.set('keyword', kw);
    location.href = url.toString();
  });
}

/* ─── ALERT ─────────────────────────────── */
function showAlert(msg, type = 'danger') {
  const existing = document.getElementById('js-alert');
  if (existing) existing.remove();
  const el = document.createElement('div');
  el.id = 'js-alert';
  el.className = `alert alert-${type}`;
  el.textContent = msg;
  el.style.cssText = 'position:fixed;top:20px;right:24px;z-index:9999;min-width:260px;animation:slideIn 0.3s ease';
  document.body.appendChild(el);
  setTimeout(() => { el.style.opacity = '0'; el.style.transition = 'opacity 0.4s'; setTimeout(() => el.remove(), 400); }, 3500);
}

/* ─── READING PROGRESS BAR ──────────────── */
function setupReadingProgress() {
  const bar = document.getElementById('reading-progress');
  if (!bar) return;
  window.addEventListener('scroll', () => {
    const total = document.documentElement.scrollHeight - window.innerHeight;
    const pct   = total > 0 ? (window.scrollY / total) * 100 : 0;
    bar.style.width = pct + '%';
  }, { passive: true });
}

/* ─── BACK TO TOP ───────────────────────── */
function setupBackToTop() {
  const btn = document.getElementById('back-to-top');
  if (!btn) return;
  window.addEventListener('scroll', () => {
    btn.style.opacity      = window.scrollY > 300 ? '1' : '0';
    btn.style.pointerEvents = window.scrollY > 300 ? 'auto' : 'none';
  }, { passive: true });
  btn.addEventListener('click', () => window.scrollTo({ top: 0, behavior: 'smooth' }));
}

/* ─── INIT ──────────────────────────────── */
document.addEventListener('DOMContentLoaded', () => {
  setupImageUpload();
  setupCategoryTabs();
  setupSearch();
  setupSidebarSearch();
  setupReadingProgress();
  setupBackToTop();
});

function setupSidebarSearch() {
  const form = document.getElementById('sidebar-search-form');
  if (!form) return;
  form.addEventListener('submit', e => {
    e.preventDefault();
    const kw = document.getElementById('sidebar-search-input').value.trim();
    if (!kw) return;
    const url = new URL('/post/list', location.origin);
    url.searchParams.set('keyword', kw);
    location.href = url.toString();
  });
}
