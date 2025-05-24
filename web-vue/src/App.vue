<template>
  <div class="app-container">
    <router-view />
  </div>
</template>
<script setup lang="ts">
// No script needed for the root App component
</script>
<style scoped>
/* Global Styles */
:root {
  --primary-color: #2C3E50;
  --secondary-color: #16A085;
  --dark-color: #1A2530;
  --light-color: #ECF0F1;
  --danger-color: #E74C3C;
  --warning-color: #F39C12;
  --success-color: #27AE60;
  --info-color: #7F8C8D;
  --background-color: #F9FAFB;
  --text-color: #2C3E50;
  --border-color: #D0D6DE;
}

/* Main App Styles */
.app-container {
  height: 100vh;
  background-color: var(--background-color);
}

</style>
