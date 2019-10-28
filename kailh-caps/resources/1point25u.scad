union () {
  difference () {
    translate ([-2.85, 0, 0]) {
      union () {
        color ([0, 1, 0, ]) {
          hull () {
            translate ([0, 0, 5]) {
              cube ([1.2, 2.9, 0.01], center=true);
            }
            translate ([0, 0, -3]) {
              translate ([0, 0, 0]) {
                cube ([1.2, 2.9, 0.01], center=true);
              }
            }
          }
        }
        color ([0, 1, 0, ]) {
          hull () {
            translate ([5.7, 0, 5]) {
              cube ([1.2, 2.9, 0.01], center=true);
            }
            translate ([0, 0, -3]) {
              translate ([5.7, 0, 0]) {
                cube ([1.2, 2.9, 0.01], center=true);
              }
            }
          }
        }
      }
    }
    translate ([0, 0, 4.5]) {
      rotate ([5.000000000000001,0.0,0.0]) {
        translate ([0, 0, 52.50260416666668]) {
          scale ([1, 2.1, 1]) {
            sphere ($fn=100, r=54.00260416666668);
          }
        }
      }
    }
  }
  difference () {
    difference () {
      hull () {
        translate ([0, 0, 4.5]) {
          rotate ([5.000000000000001,0.0,0.0]) {
            minkowski () {
              cube ([11.875, 8.3, 0.0099], center=true);
              cylinder ($fs=0.1, h=1.0E-4, r=3, center=true);
            }
          }
        }
        minkowski () {
          cube ([20.875, 16.5, 0.0099], center=true);
          cylinder ($fs=0.1, h=1.0E-4, r=0.5, center=true);
        }
      }
      translate ([0, 0, 4.5]) {
        rotate ([5.000000000000001,0.0,0.0]) {
          translate ([0, 0, 52.50260416666668]) {
            scale ([1, 2.1, 1]) {
              sphere ($fn=100, r=54.00260416666668);
            }
          }
        }
      }
    }
    #union () {
      hull () {
        rotate ([5.000000000000001,0.0,0.0]) {
          translate ([0, 0, 1.5]) {
            minkowski () {
              cube ([10.375, 6.800000000000001, 0.0099], center=true);
              cylinder ($fs=0.1, h=1.0E-4, r=3, center=true);
            }
          }
        }
        translate ([0, 0, -0.001]) {
          minkowski () {
            cube ([19.375, 15.0, 0.0099], center=true);
            cylinder ($fs=0.1, h=1.0E-4, r=0.5, center=true);
          }
        }
      }
    }
  }
}
