#!/usr/bin/python3
import numpy as np
from matplotlib import gridspec
import matplotlib.pyplot as plt

# colors and hatch styles
colors = ['steelblue', 'darkorange', 'wheat']
line_hatches = ['--', ':', '-']
line_widths = [1.2, 1.2, 2.8]

# create figure and gridspec
fig, ax = plt.subplots(4, 1, figsize=(7.8, 8))

data_files = ['data/lattice-100/0-129.82.208.110.nmon.csv', 'data/lattice-100/1-129.82.208.111.nmon.csv', 'data/lattice-100/2-129.82.208.112.nmon.csv', 'data/lattice-100/3-129.82.208.113.nmon.csv', 'data/lattice-100/4-129.82.208.114.nmon.csv']
legend_plots=[]

for i in range(len(data_files)):
    # import data
    data = np.genfromtxt(data_files[i],
        delimiter=',', names=True, skip_header=0)

    # scale values
    #for j in range(len(data)):
        #data['record'][j] = data['record'][j] / 60.0
    data['timestamp'] = (data['timestamp'] - 1615481883) / 60.0
        #data['NETeno1writeKBs'][j] = \
        #    data['NETeno1writeKBs'][j] / 1000000.0
    data['CPU_ALLUser'] = data['CPU_ALLUser'] + data['CPU_ALLSys']
    #data['MEMactive'] = (data['MEMmemtotal'] - data['MEMinactive']) / 1000
    data['MEMactive'] = data['MEMactive'] / 1000
    data['NETeno1writeKBs'] = \
        data['NETeno1writeKBs'] / 1000.0
    data['NETeno1readKBs'] = \
        data['NETeno1readKBs'] / 1000.0

    # plot data
    #p = ax.plot(data['record'], data['NETeno1writeKBs'], \
    p = ax[0].plot(data['timestamp'], data['CPU_ALLUser'], \
        line_hatches[0], color=colors[0], linewidth=line_widths[0])
    p = ax[1].plot(data['timestamp'], data['MEMactive'], \
        line_hatches[0], color=colors[0], linewidth=line_widths[0])
    p = ax[2].plot(data['timestamp'], data['NETeno1writeKBs'], \
        line_hatches[0], color=colors[0], linewidth=line_widths[0])
    p = ax[3].plot(data['timestamp'], data['NETeno1readKBs'], \
        line_hatches[0], color=colors[0], linewidth=line_widths[0])

    legend_plots.append(p[0])

# set subplot 0 parameters
ax[0].set_title('Representative CPU', fontsize=11, fontweight='bold')
ax[0].set_ylabel('CPU Utilization (%)', fontsize=11)
ax[0].grid(linestyle=':')

ax[1].set_title('Representative RAM', fontsize=11, fontweight='bold')
ax[1].set_ylabel('Memory Utilization (GB)', fontsize=11)
ax[1].grid(linestyle=':')

ax[2].set_title('Representative Network I/O: eno1-write', fontsize=11, fontweight='bold')
ax[2].set_ylabel('Network I/O (MB/s)', fontsize=11)
ax[2].grid(linestyle=':')

ax[3].set_title('Representative Network I/O: eno1-read', fontsize=11, fontweight='bold')
ax[3].set_xlabel('Time (Minutes)', fontsize=11)

#plt.legend(legend_plots, ('Envoy', 'Sustain Query Service'), \
#    loc='upper right', fontsize=11, handlelength=3, ncol=1)

# save figure
#fig.set_size_inches(w=12, h=3)
fig.tight_layout()
fig.savefig('/tmp/envoy-sqs-all.pdf', dpi=300)
