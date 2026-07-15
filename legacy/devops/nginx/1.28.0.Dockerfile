FROM nginx:alpine
#COPY ./devops/nginx/default.conf /etc/nginx/conf.d/default.conf
COPY ./HIS-WAR/src/main/webapp /usr/share/nginx/html
