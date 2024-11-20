const proxyConfigMappings: Record<ProxyType, ProxyConfig> = {
  dev: {
    prefix: '/api',
    target: 'http://localhost:9090',
  },
  test: {
    prefix: '/api',
    target: 'http://localhost:9090',
  },
  prod: {
    prefix: '/api',
    target: 'http://localhost:9090',
  },
}

export function getProxyConfig(envType: ProxyType = 'dev'): ProxyConfig {
  return proxyConfigMappings[envType]
}
