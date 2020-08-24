const axios = require('axios');

const localInstance = axios.create({
  baseURL: 'http://localhost:8080/',
  timeout: 1000,
});

export { localInstance };
