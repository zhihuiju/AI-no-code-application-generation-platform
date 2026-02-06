<template>
  <div class="global-header">
    <div class="header-content">
      <div class="header-left">
        <div class="logo">
          <img src="@/assets/logo.png" alt="Logo" />
          <span class="site-title">AIGineer</span>
        </div>
        <div class="header-menu">
          <router-link to="/">首页</router-link>
          <router-link to="/about">关于</router-link>
          <router-link to="/features">功能</router-link>
          <router-link to="/docs">文档</router-link>
        </div>
      </div>
      <div class="header-right">
        <button class="login-btn">登录</button>
      </div>
    </div>
  </div>
</template>

<script setup></script>

<style scoped>
.global-header {
  position: sticky;
  top: 0;
  z-index: 1000;
  background-color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  height: 64px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
  margin: 0 auto;
  padding: 0 24px;
  max-width: 1200px;
  width: 100%;
  box-sizing: border-box;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 32px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo img {
  width: 32px;
  height: 32px;
}

.site-title {
  font-size: 18px;
  font-weight: 600;
  color: #1890ff;
}

.header-menu {
  display: flex;
  gap: 24px;
}

.header-menu a {
  text-decoration: none;
  color: #333;
  font-size: 14px;
}

.header-menu a:hover {
  color: #1890ff;
}

.header-right {
  display: flex;
  align-items: center;
}

.login-btn {
  padding: 6px 16px;
  background-color: transparent;
  border: 1px solid #1890ff;
  color: #1890ff;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.login-btn:hover {
  background-color: #e6f7ff;
}

@media (max-width: 768px) {
  .header-content {
    padding: 0 16px;
  }

  .header-left {
    gap: 16px;
  }

  .site-title {
    display: none;
  }

  .header-menu {
    display: none;
  }
}
</style>
