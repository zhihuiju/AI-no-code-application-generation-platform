<template>
  <div class="global-footer">
    <div class="footer-content">
      <p class="copyright">
        AIGineer by 程序员韬
        <a
          href="https://github.com/zhihuiju/AI-no-code-application-generation-platform"
          target="_blank"
          rel="noopener noreferrer"
          class="github-link"
        >
          (GitHub)
        </a>
      </p>
    </div>
  </div>
</template>

<script setup></script>

<style scoped>
.global-footer {
  margin-top: auto;
  background-color: #f5f5f5;
  border-top: 1px solid #e8e8e8;
  padding: 24px 0;
}

.footer-content {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 0 auto;
  max-width: 1200px;
  width: 100%;
  box-sizing: border-box;
}

.copyright {
  font-size: 14px;
  color: #666;
  margin: 0;
  text-align: center;
}

.github-link {
  color: #1890ff;
  text-decoration: none;
  margin-left: 8px;
}

.github-link:hover {
  text-decoration: underline;
}

@media (max-width: 768px) {
  .footer-content {
    padding: 0 16px;
  }

  .copyright {
    font-size: 12px;
  }
}
</style>
