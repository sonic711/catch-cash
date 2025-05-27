<template>
  <div class="app-container">
    <router-view />
  </div>
</template>
<script setup lang="ts">
// No script needed for the root App component
</script>
<style scoped>

/* Main App Styles */
.app-container {
  min-height: 100vh;
  background-color: var(--background-color);
}

</style>
