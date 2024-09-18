/** @type {import('tailwindcss').Config} */
module.exports = {
  // prefix: 'tw-',
  content: [
    "./src/**/*.{html,ts}",
     "./node_modules/flowbite/**/*.js"
  ],
  theme: {
    extend: {},
  },
  plugins: [
    require('flowbite/plugin')]
}

