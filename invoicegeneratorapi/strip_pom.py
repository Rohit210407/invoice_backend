import re

pom_path = r"c:\Users\rohit\Downloads\INVOICE PROJECT\invoice-gen-web-app\invoicegeneratorapi\pom.xml"
with open(pom_path, 'r', encoding='utf-8') as f:
    c = f.read()

# Strip lombok dependency
c = re.sub(r'\s*<dependency>\s*<groupId>org\.projectlombok</groupId>.*?<artifactId>lombok</artifactId>.*?</dependency>', '', c, flags=re.DOTALL)

# Strip lombok annotationProcessorPath
c = re.sub(r'\s*<path>\s*<groupId>org\.projectlombok</groupId>.*?<artifactId>lombok</artifactId>.*?</path>', '', c, flags=re.DOTALL)
c = re.sub(r'\s*<annotationProcessorPaths>\s*</annotationProcessorPaths>', '', c, flags=re.DOTALL)

with open(pom_path, 'w', encoding='utf-8') as f:
    f.write(c)
print("Stripped lombok from pom.xml")
