FROM node:12 as builder
COPY package.json package-lock.json ./
RUN npm install && mkdir /app && mv ./node_modules ./app
WORKDIR /app
COPY . .
RUN npm run build 

FROM nginx:1.18.0-alpine
COPY ./.nginx/nginx.conf /etc/nginx/
COPY ./.nginx/ssl/bundle.crt ./.nginx/ssl/private.key /etc/ssl/
RUN rm -rf /usr/share/nginx/html/*
COPY --from=builder /app/dist/snsr-front-app /usr/share/nginx/html
ENTRYPOINT ["nginx", "-g", "daemon off;"]
