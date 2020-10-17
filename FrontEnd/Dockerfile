FROM node:14.11.0

WORKDIR /app

ENV PATH /app/node_modules/.bin:$PATH

COPY package*json ./

RUN npm install

COPY . .

EXPOSE 3000/tcp
EXPOSE 80/tcp

ENV CI true

CMD [ "npm", "start" ]