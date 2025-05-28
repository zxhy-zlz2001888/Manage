<template>
  <div class="about-container ui-card">
    <h2 class="title">✍️ 关于我们</h2>
    <p>本系统由 <b>zixu</b> 制作，技术选型为 <span class="highlight">Vite + Vue3 + Axios</span>。</p>
    <p>初衷：为练习和学习现代前端提供实践机会。</p>
    <p>联系方式：<span class="highlight">wangzixu61868@gmail.com</span></p>
  </div>
</template>
<style scoped>
.about-container {
  max-width: 420px;margin:60px auto;
  background:#fcfcfc;
  padding:35px 32px 32px 32px;
  border-radius:12px;
  box-shadow: 0 4px 24px #0002;
}
.title {
  font-size: 1.4rem;
  text-align: center;
  margin-bottom: 19px;
  font-weight: bold;
  color: #3066cf;
  letter-spacing: 1.5px;
}
p {
  color: #3b4d6c;
  margin-bottom: 13px;
  font-size: 1.08em;
  line-height: 1.62;
}
.highlight {
  background: #ecf9ff;
  color: #2277cc;
  padding: 1px 5px;
  border-radius: 3px;
}
</style>