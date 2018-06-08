mvn clean package -Dmaven.test.skip=true

# 拷贝文件到目标目录
rm -rdf ./target/release
mkdir ./target/release
cp ./target/gitprd*.jar ./target/release/gitprd.jar
cp -R ./src/main/resources/static ./target/release/

# mac app打包: http://centerkey.com/mac/java/
jdk=$(/usr/libexec/java_home)
$jdk/bin/javapackager \
   -deploy \
   -native image \
   -name gitprd \
   -BappVersion=0.0.0 \
   -Bicon=app.icns \
   -srcdir ./target/release \
   -srcfiles gitprd.jar:static/html/index.html \
   -appclass org.springframework.boot.loader.JarLauncher \
   -outdir target/release/result \
   -outfile gitprd \
   -nosign \
   -v


open /Users/clive/data/code/GitPRD/target/release/result/bundles/gitprd.app
