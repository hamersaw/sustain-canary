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

data_files = [
    'data/lattice-100/10-129.82.208.120.nmon.csv',
    'data/lattice-100/11-129.82.208.121.nmon.csv',
    'data/lattice-100/12-129.82.208.122.nmon.csv',
    'data/lattice-100/13-129.82.208.123.nmon.csv',
    'data/lattice-100/14-129.82.208.124.nmon.csv',
    'data/lattice-100/15-129.82.208.125.nmon.csv',
    'data/lattice-100/16-129.82.208.126.nmon.csv',
    'data/lattice-100/17-129.82.208.127.nmon.csv',
    'data/lattice-100/18-129.82.208.128.nmon.csv',
    'data/lattice-100/19-129.82.208.129.nmon.csv',
    'data/lattice-100/20-129.82.208.130.nmon.csv',
    'data/lattice-100/21-129.82.208.131.nmon.csv',
    'data/lattice-100/22-129.82.208.132.nmon.csv',
    'data/lattice-100/23-129.82.208.133.nmon.csv',
    'data/lattice-100/24-129.82.208.134.nmon.csv',
    'data/lattice-100/25-129.82.208.135.nmon.csv',
    'data/lattice-100/26-129.82.208.136.nmon.csv',
    'data/lattice-100/27-129.82.208.137.nmon.csv',
    'data/lattice-100/28-129.82.208.138.nmon.csv',
    'data/lattice-100/29-129.82.208.139.nmon.csv',
    'data/lattice-100/30-129.82.208.140.nmon.csv',
    'data/lattice-100/31-129.82.208.141.nmon.csv',
    'data/lattice-100/32-129.82.208.142.nmon.csv',
    'data/lattice-100/33-129.82.208.143.nmon.csv',
    'data/lattice-100/34-129.82.208.144.nmon.csv',
    'data/lattice-100/35-129.82.208.145.nmon.csv',
    'data/lattice-100/36-129.82.208.146.nmon.csv',
    'data/lattice-100/37-129.82.208.147.nmon.csv',
    'data/lattice-100/38-129.82.208.148.nmon.csv',
    'data/lattice-100/39-129.82.208.149.nmon.csv',
    'data/lattice-100/40-129.82.208.150.nmon.csv',
    'data/lattice-100/41-129.82.208.151.nmon.csv',
    'data/lattice-100/42-129.82.208.152.nmon.csv',
    'data/lattice-100/43-129.82.208.153.nmon.csv',
    'data/lattice-100/44-129.82.208.154.nmon.csv',
    'data/lattice-100/45-129.82.208.155.nmon.csv',
    'data/lattice-100/46-129.82.208.156.nmon.csv',
    'data/lattice-100/47-129.82.208.157.nmon.csv',
    'data/lattice-100/48-129.82.208.158.nmon.csv',
    'data/lattice-100/49-129.82.208.159.nmon.csv',
    'data/lattice-100/5-129.82.208.115.nmon.csv',
    'data/lattice-100/6-129.82.208.116.nmon.csv',
    'data/lattice-100/7-129.82.208.117.nmon.csv',
    'data/lattice-100/8-129.82.208.118.nmon.csv',
    'data/lattice-100/9-129.82.208.119.nmon.csv']
legend_plots=[]

for i in range(len(data_files)):
    # import data
    data = np.genfromtxt(data_files[i],
        delimiter=',', names=True, skip_header=0)

    # scale values
    #for j in range(len(data)):
        #data['record'][j] = data['record'][j] / 60.0
    data['timestamp'] = (data['timestamp'] - 1615481883) / 60.0
    data['CPU_ALLUser'] = data['CPU_ALLUser'] + data['CPU_ALLSys']
    #data['MEMactive'] = (data['MEMmemtotal'] - data['MEMinactive']) / 1000
    data['MEMactive'] = data['MEMactive'] / 1000
        #data['NETeno1writeKBs'][j] = \
        #    data['NETeno1writeKBs'][j] / 1000000.0
    data['DISKREADsda'] = data['DISKREADsda'] / 1000.0
    data['DISKREADsdd'] = data['DISKREADsdd'] / 1000.0

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
    p = ax[2].plot(data['timestamp'], data['DISKREADsda'], \
        line_hatches[0], color=colors[0], linewidth=line_widths[0])
    #p = ax[3].plot(data['timestamp'], data['DISKREADsdd'], \
    #    line_hatches[0], color=colors[0], linewidth=line_widths[0])
    p = ax[3].plot(data['timestamp'], data['NETeno1writeKBs'], \
        line_hatches[0], color=colors[0], linewidth=line_widths[0])

    legend_plots.append(p[0])

# set subplot 0 parameters
ax[0].set_title('Representative MongoD CPU', fontsize=11, fontweight='bold')
ax[0].set_ylabel('CPU Utilization (%)', fontsize=11)
ax[0].grid(linestyle=':')

ax[1].set_title('Representative MongoD RAM', fontsize=11, fontweight='bold')
ax[1].set_ylabel('Memory Utilization (GB)', fontsize=11)
ax[1].grid(linestyle=':')

ax[2].set_title('Representative MongoD Disk I/O (/dev/sda)', fontsize=11, fontweight='bold')
ax[2].set_ylabel('Disk Read (MB/s)', fontsize=11)
ax[2].grid(linestyle=':')

#ax[3].set_title('Representative MongoD Disk I/O (/dev/sdd)', fontsize=11, fontweight='bold')
#ax[3].set_ylabel('Disk Read (MB/s)', fontsize=11)
#ax[3].set_xlabel('Time (Minutes)', fontsize=11)
#ax[3].grid(linestyle=':')

ax[3].set_title('Representative Spark Network I/O: eno1-write', fontsize=11, fontweight='bold')
ax[3].set_ylabel('Network I/O (MB/s)', fontsize=11)
ax[3].set_xlabel('Time (Minutes)', fontsize=11)
ax[3].grid(linestyle=':')

#plt.legend(legend_plots, ('Envoy', 'Sustain Query Service'), \
#    loc='upper right', fontsize=11, handlelength=3, ncol=1)

# save figure
#fig.set_size_inches(w=12, h=3)
fig.tight_layout()
fig.savefig('/tmp/mongod-all.pdf', dpi=300)
