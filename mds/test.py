import numpy as np
import matplotlib.pyplot as plt
import out

from sklearn.manifold import MDS

mds_sklearn = MDS(n_components=3, metric=True, dissimilarity='precomputed', normalized_stress=False)
init = None# out.initial
x_sklearn = mds_sklearn.fit_transform(out.stitches, init=init)
print(x_sklearn)

fig = plt.figure(figsize=(6, 6))
ax = fig.add_subplot(projection='3d')
ax.set_box_aspect((np.ptp(x_sklearn[:, 0]), np.ptp(x_sklearn[:, 1]), np.ptp(x_sklearn[:, 2])))

for i in range(len(x_sklearn)):  # plot each point + its index as text above
    # ax.scatter(out.initial[i, 0], out.initial[i, 1], out.initial[i, 2], color='r')
    ax.scatter(x_sklearn[i, 0], x_sklearn[i, 1], x_sklearn[i, 2], color='b')
    # ax.text(x_sklearn[i, 0], x_sklearn[i, 1], x_sklearn[i, 2], '%s' % (str(i)), size=10, zorder=1, color='k')
plt.title('Metric MDS, taxi-cab')
plt.show()
print(mds_sklearn.stress_)
