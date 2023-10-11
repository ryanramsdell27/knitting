import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import out
# df = pd.DataFrame(stitches)

# from sklearn.preprocessing import MinMaxScaler
#
# scaler = MinMaxScaler()
# x_scaled = scaler.fit_transform(df)

from sklearn.manifold import MDS

mds_sklearn = MDS(n_components=3, metric=False, dissimilarity='precomputed', normalized_stress=True)
x_sklearn = mds_sklearn.fit_transform(out.stitches, init=out.initial)
print(x_sklearn)

fig = plt.figure(figsize=(6, 6))
ax = fig.add_subplot(projection='3d')
ax.set_box_aspect((np.ptp(x_sklearn[:, 0]), np.ptp(x_sklearn[:, 1]), np.ptp(x_sklearn[:, 2])))

# ax.scatter(x_sklearn[:, 0], x_sklearn[:, 1], x_sklearn[:, 2])
# scale = 0
# t = 0
# for x in range(len(x_sklearn)):
#     for y in range(len(x_sklearn[0])):
#         if initial[x, y] != 0:
#             scale += x_sklearn[x, y] / initial[x, y]
#             t += 1
#             print(scale/t)
# scale /= t
# scale = 0.2
for i in range(len(x_sklearn)):  # plot each point + it's index as text above
    ax.scatter(x_sklearn[i, 0], x_sklearn[i, 1], x_sklearn[i, 2], color='b')
    # ax.scatter(scale * initial[i, 0], scale * initial[i, 1], scale * initial[i, 2], color='r')
    ax.text(x_sklearn[i, 0], x_sklearn[i, 1], x_sklearn[i, 2], '%s' % (str(i)), size=10, zorder=1,
            color='k')
plt.title('Non Metric MDS, immediate neighbor')
# for x, y, z in zip(x_sklearn[:, 0], x_sklearn[:, 1], x_sklearn[:, 2]):
#     ax.annotate(
#         label,
#         xy=(x, y),
#         xytext=(-10, 10),
#         textcoords='offset points'
#     )
plt.show()
print(mds_sklearn.stress_)
