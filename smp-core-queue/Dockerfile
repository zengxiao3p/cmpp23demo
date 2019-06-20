FROM centos

MAINTAINER laicuiping
##����jdk
ADD jdk-8u201-linux-x64.tar.gz  /
#���spring boot С��Ŀ����������Ϊapp.jar'
ADD target/smp-core-queue-1.0.0-RELEASES.jar  app.jar

##����jdk����
ENV JAVA_HOME=/jdk1.8.0_201
ENV PATH=${JAVA_HOME}/bin:$PATH
ENV CLASSPATH=.:${JAVA_HOME}/lib/dt.jar:${JAVA_HOME}/lib/tools.jar

EXPOSE 8090
##ָ��ִ������spring boot��Ŀ
ENTRYPOINT ["java","-jar","/app.jar"]