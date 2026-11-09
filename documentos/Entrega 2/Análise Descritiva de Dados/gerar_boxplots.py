"""Recalcula figura a partir da base fictícia; requer pandas e matplotlib."""
from pathlib import Path
import pandas as pd
import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt

p = Path(__file__).resolve().parent
data = pd.read_csv(p / 'dados_simulados_alunos.csv')
columns = ['Idade (anos)', 'Carga Horária (h)', 'Frequência (%)']
fig, axes = plt.subplots(1, 3, figsize=(11.7, 5.5))
for ax, column in zip(axes, columns):
    values = data[column]
    ax.boxplot(values, patch_artist=True, whis=1.5,
               boxprops=dict(facecolor='#cde5df'),
               medianprops=dict(color='#00524b', linewidth=2))
    ax.set_xticks([])
    ax.set_title(column)
    ax.set_ylabel(column.split('(')[-1].rstrip(')'))
    ax.grid(axis='y', alpha=.2)
    q1, q3 = values.quantile([.25, .75])
    iqr = q3 - q1
    count = ((values < q1 - 1.5 * iqr) | (values > q3 + 1.5 * iqr)).sum()
    cv = 100 * values.std(ddof=1) / values.mean()
    label = f'CV amostral: {cv:.2f}% | outliers: {count}'.replace('.', ',')
    ax.text(.5, -.1, label, ha='center', transform=ax.transAxes, fontsize=9)
fig.suptitle('Próxima Etapa - dispersão em 50 registros simulados', fontsize=16)
fig.text(.5, .035, 'Quartis inclusivos | Tukey: 1,5 x IQR | Dados fictícios, sem resultados reais da ONG', ha='center', fontsize=9)
fig.tight_layout(rect=[0, .09, 1, .92])
fig.savefig(p / 'boxplots_recalculados.png', dpi=180)
fig.savefig(p / 'boxplots_recalculados.pdf')
plt.close(fig)
