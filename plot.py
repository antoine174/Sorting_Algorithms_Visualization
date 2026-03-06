import matplotlib
try:matplotlib.use('TkAgg')
except:pass
import matplotlib.pyplot as plt
import pandas as pd
import seaborn as sns

f='comparison_results_file.csv'
with open(f,'r') as file:
    lines=file.readlines()

stop=len(lines)
for i,l in enumerate(lines):
    if "final conc" in l.lower() or "summary" in l.lower():
        stop=i
        break

df=pd.read_csv(f,nrows=stop-1)
df.columns=df.columns.str.strip()

for c in ['Avg Time (ms)','Comparisons','Interchanges','Array Size']:
    df[c]=pd.to_numeric(df[c],errors='coerce')

sns.set_style("whitegrid")
fig,ax=plt.subplots(1,3,figsize=(15,5))

sns.barplot(data=df,x='Algorithm',y='Avg Time (ms)',hue='Array Size',ax=ax[0])
ax[0].set_title('Time (ms)')
ax[0].tick_params(axis='x',rotation=45)

sns.barplot(data=df,x='Algorithm',y='Comparisons',hue='Array Size',ax=ax[1])
ax[1].set_title('Comparisons')
ax[1].tick_params(axis='x',rotation=45)

sns.barplot(data=df,x='Algorithm',y='Interchanges',hue='Array Size',ax=ax[2])
ax[2].set_title('Interchanges')
ax[2].tick_params(axis='x',rotation=45)

plt.tight_layout()
try:
    plt.show()
except:
    plt.savefig('run_plots.png')