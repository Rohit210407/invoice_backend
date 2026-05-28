import os, re
src = r"c:\Users\rohit\Downloads\INVOICE PROJECT\invoice-gen-web-app\invoicegeneratorapi\src\main\java\in\bushansirgur\invoicegeneratorapi"

for root, _, files in os.walk(src):
    for f in files:
        if not f.endswith(".java"): continue
        p = os.path.join(root, f)
        with open(p, 'r', encoding='utf-8') as file:
            c = file.read()
        
        if "@RequiredArgsConstructor" in c:
            cls_match = re.search(r'public class (\w+)', c)
            if not cls_match: continue
            cls_name = cls_match.group(1)
            
            # Find private final fields
            # It matches something like "private final UserRepository userRepository;"
            fields = re.findall(r'private final\s+([A-Za-z0-9_<>]+)\s+([A-Za-z0-9_]+)\s*;', c)
            
            if fields:
                params = ", ".join([f"{t} {n}" for t, n in fields])
                assigns = "\n        ".join([f"this.{n} = {n};" for t, n in fields])
                ctor = f"\n    public {cls_name}({params}) {{\n        {assigns}\n    }}\n"
                
                c = re.sub(r'@RequiredArgsConstructor\s*', '', c)
                c = re.sub(r'import lombok\.RequiredArgsConstructor;\s*', '', c)
                
                last_field = f"private final {fields[-1][0]} {fields[-1][1]};"
                c = c.replace(last_field, last_field + "\n" + ctor)
                
                with open(p, 'w', encoding='utf-8') as file:
                    file.write(c)
                print(f"Refactored {f}")
