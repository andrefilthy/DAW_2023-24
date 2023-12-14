FROM node:12
WORKDIR ./app
COPY ["package.json", "tsconfig.json", "webpack.config.js", "./"]
COPY ./src ./src
COPY ./public ./public


RUN npm install

CMD ["npm", "start"]