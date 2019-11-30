import pylab as pl
import json
import numpy as np

stats = json.load(open("/tmp/stats.json"))

ax = None

def doPlot(x):
    a,b = zip(*x['results'])
    a = [ int(x) for x in a]
    pl.xscale('log')
    pl.yscale('log')
    pl.plot(a,b,
            x['fmt'],
            label = x['name'] , alpha = 0.1)

for x in stats:
    x['results'] = list(x['results'].items())
    x['results'].sort( key = lambda x: int(x[0]))
    x['results'] = [ (q,int(v)) for q,v in x['results'] ]

def norm(d):
    ret = [], []
    for (q,v) in d:
        iq = int(q)
        for _ in range(v):
            ret[0].append(int(q))
            ret[1].append(iq)
    ary = np.array(ret[1])
    print(ary)
    ary = ary / ary.sum()
    return np.array(ret[0]), ary

if 0:
    pl.figure('all')
    pl.title('all')
    pl.figure('sub')
    pl.title('sub')
    for (idx, x) in enumerate(stats):
        pl.figure('sub')
        if ax:
            pl.subplot( 2,2, idx+1, sharex = ax, sharey = ax, title = x['name'])
        else:
            ax = pl.subplot( 2,2, idx+1, title = x['name'])
        doPlot(x)

    pl.figure('all')
    pl.clf()
    for (idx, x) in enumerate(stats):
        doPlot(x)


pl.figure('cum')
pl.clf()
pl.title('cum')
for x in stats:
    xs, ys = norm(x['results'])
    ycum = np.cumsum(ys)
    pl.plot(xs, ycum, label = x['name'])

pl.legend()
 
